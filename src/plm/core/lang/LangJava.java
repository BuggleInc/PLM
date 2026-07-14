package plm.core.lang;

import java.awt.*;
import java.io.*;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import org.checkerframework.checker.nullness.qual.NonNull;
import plm.core.PLMCompilerException;
import plm.core.lang.primitives.ExternalPrimitiveLanguage;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveParameter;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.session.SourceFile;
import plm.core.ui.ResourcesCache;
import plm.universe.CommandExecutor;
import plm.universe.Direction;
import plm.universe.Entity;

public class LangJava extends JVMCompiledLang {
  /**
   * Extra source files to be copied alongside the student's code
   */
  private static final Map<String, List<String>> remoteExtraSourceFiles = Map.of("RemoteCons", List.of("src/lessons/recursion/cons/universe/RecList.java"));
  /* Language detection logic */
  private static String brokenLanguageMessage;
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;
  File tempFolder                                        = new File(System.getProperty("java.io.tmpdir"), "plm_java_toremove");

  public LangJava() { super("Java", "java", ResourcesCache.getIcon("img/lang_java.png")); }

  /**
   * e.g. "src/lessons/recursion/cons/universe/RecList.java" -> "lessons.recursion.cons.universe.RecList"
   */
  private static String fqcnFromSourcePath(String sourcePath)
  {
    String withoutSrcPrefix = sourcePath.startsWith("src/") ? sourcePath.substring("src/".length()) : sourcePath;
    String withoutExtension =
        withoutSrcPrefix.endsWith(".java") ? withoutSrcPrefix.substring(0, withoutSrcPrefix.length() - ".java".length()) : withoutSrcPrefix;
    return withoutExtension.replace('/', '.');
  }

  /**
   * e.g. "src/lessons/recursion/cons/universe/RecList.java" -> "RecList"
   */
  private static String fileNameWithoutExtension(String path)
  {
    String name = new File(path).getName();
    int dot     = name.lastIndexOf('.');
    return dot < 0 ? name : name.substring(0, dot);
  }

  private static @NonNull String getCorrectedTemplate(String correction)
  {

    int runFunctionI = correction.indexOf("void run(");

    int beginTemplateIndex = correction.indexOf("/* BEGIN TEMPLATE */") + "/* BEGIN TEMPLATE */".length();
    int endTemplateIndex   = correction.indexOf("/* END TEMPLATE */");
    int beginSolutionIndex = correction.indexOf("/* BEGIN SOLUTION */");

    String template;
    if (beginSolutionIndex == beginTemplateIndex + 3)
      template = "$package\n\n$imports\n\npublic class Entity {\n$dependency\n\t public void run(){\n$body}\n}";
    else {
      if (beginTemplateIndex < runFunctionI && runFunctionI < endTemplateIndex)
        template = "$package\n\n$imports\n\npublic class Entity {\n$dependency\n\t\n$body\n}";
      else
        template = "$package\n\n$imports\n\npublic class Entity {\n$dependency\n$run\n\t\n$body\n}";
    }
    return template;
  }

  private static String extractRunFunction(String code)
  {
    int startRun       = code.indexOf("void run(");
    int beginOfRunLine = code.substring(0, startRun).lastIndexOf('\n');
    if (beginOfRunLine == -1)
      beginOfRunLine = 0;

    int i       = code.indexOf('{', startRun) + 1;
    int bracket = 1;

    for (; i < code.length() && bracket > 0; i++) {
      if (code.charAt(i) == '{')
        bracket++;
      if (code.charAt(i) == '}')
        bracket--;
    }

    String substring = code.substring(beginOfRunLine, i);
    return substring;
  }

  private static String extractRunDependency(String code)
  {

    StringBuilder section = new StringBuilder();

    for (int i = 0; i < code.length(); i++) {
      if (!code.startsWith("/* BEGIN DEPENDENCY */", i))
        continue;

      int begin = i + "/* BEGIN DEPENDENCY */".length();
      int end   = code.indexOf("/* END DEPENDENCY */", i);

      section.append(code, begin, end).append("\n");
      i = end + "/* END DEPENDENCY */".length();
    }

    return section.toString();
  }

  private static String extractImportDependency(String code)
  {

    StringBuilder section = new StringBuilder();

    for (int i = 0; i < code.length(); i++) {
      if (!code.startsWith("/* BEGIN IMPORT */", i))
        continue;

      int begin = i + "/* BEGIN IMPORT */".length();
      int end   = code.indexOf("/* END IMPORT */", i);

      section.append(code, begin, end).append("\n");
      i = end + "/* END IMPORT */".length();
    }

    return section.toString();
  }

  private static String getRemote(String code)
  {
    if (code.contains("plm.test.simple"))
      return "RemoteSimple";
    if (code.contains(".bat."))
      return "RemoteBat";
    if (code.contains(".cons."))
      return "RemoteCons";
    if (code.contains("Buggle") || code.contains("Langton") || code.contains("Turmite"))
      return "RemoteBuggle";
    if (code.contains("Turtle"))
      return "RemoteTurtle";
    if (code.contains("Flag"))
      return "RemoteFlag";
    if (code.contains("Baseball"))
      return "RemoteBaseball";
    if (code.contains("Pancake"))
      return "RemotePancake";
    if (code.contains("Hanoi"))
      return "RemoteHanoi";
    if (code.contains("Sort"))
      return "RemoteSort";
    //      if (code.contains("Lander"))
    //        return "RemoteLander";

    return null;
  }

  private static void compileJavaFiles(DiagnosticCollector<JavaFileObject> diagnostic, File packageFolder, File... files) throws PLMCompilerException
  {
    Runtime rt = Runtime.getRuntime();

    for (File javaFile : files) {

      String path = javaFile.toPath().toString();
      if (!path.endsWith(".java")) {
        throw new PLMCompilerException("Trying to compile a non java file: '" + path + "'", Set.of(path), new Error(), diagnostic);
      }
    }

    List<String> names = Arrays.asList(files).stream().map(s -> s.getName()).toList();
    List<String> paths = Arrays.asList(files).stream().map(s -> s.toPath().toString()).toList();

    ArrayList<String> args = new ArrayList<>();

    args.add("javac");
    args.addAll(names);

    Process proc;
    try {
      proc = rt.exec(args.toArray(String[] ::new), new String[] {}, packageFolder);

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

      String rtStdout = stdInput.lines().collect(Collectors.joining("\n"));
      String rtStderr = stdError.lines().collect(Collectors.joining("\n"));

      if (!rtStderr.isEmpty()) {
        throw new PLMCompilerException(rtStderr, new HashSet<>(paths), new Error(), diagnostic);
      }
    } catch (IOException e) {
      throw new PLMCompilerException(e.getMessage(), new HashSet<>(paths), new Error(), diagnostic);
    }
  }

  private static void createJarFile(DiagnosticCollector<JavaFileObject> diagnostic, File root, File packageFolder, File jarFile, File mainFile, File... files)
      throws PLMCompilerException
  {

    Set<File> allFiles = new HashSet<>();
    allFiles.add(mainFile);
    allFiles.addAll(List.of(files));

    Runtime rt = Runtime.getRuntime();

    if (!packageFolder.toPath().toString().startsWith(root.toPath().toString())) {
      throw new PLMCompilerException("Root folder (" + root.toPath() + ") is not above package folder (" + packageFolder.toPath() + ") in file hierarchy.",
                                     Set.of(), new Error(), diagnostic);
    }

    String packagePath = packageFolder.toPath().toString().substring(root.toPath().toString().length() + 1);
    String packageName = packagePath.replace('/', '.');

    String mainFileDotPath = packageName + "." + mainFile.getName().substring(0, mainFile.getName().indexOf('.'));

    File manifestFile = new File(packageFolder, "MANIFEST.MF");
    try {
      Files.writeString(manifestFile.toPath(), "Main-Class: " + mainFileDotPath + "\n");

      List<String> classFiles = allFiles.stream()
                                    .map(s -> {
                                      String javaPath = s.toPath().toString();
                                      return javaPath.substring(0, javaPath.lastIndexOf('.')) + ".class";
                                    })
                                    .filter(s -> s.endsWith(".class"))
                                    .toList();
      classFiles = classFiles.stream().map(s -> s.substring(root.toPath().toString().length() + 1)).toList();

      ArrayList<String> args = new ArrayList<>();

      args.add("jar");
      args.add("cfm");
      args.add(jarFile.toPath().toString());
      args.add(manifestFile.toPath().toString());
      args.addAll(classFiles);

      Process proc = rt.exec(args.toArray(String[] ::new), new String[] {}, root);

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

      String rtStdout = stdInput.lines().collect(Collectors.joining("\n"));
      String rtStderr = stdError.lines().collect(Collectors.joining("\n"));

      if (!rtStderr.isEmpty()) {
        throw new PLMCompilerException(rtStderr, allFiles.stream().map(s -> s.toPath().toString()).collect(Collectors.toSet()), new Error(), diagnostic);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override public boolean isJava() { return true; }

  @Override public String getBrokenLanguageMessage() { return brokenLanguageMessage; }

  @Override public boolean isBrokenLanguage()
  {
    if (brokenLanguageState == BrokenLanguageState.Unitialized) {
      throw new RuntimeException("Unimplemented");
    }
    return brokenLanguageState != BrokenLanguageState.Usable;
  }

  public String getRemoteJavaFile(String remoteName, String packageName)
  {
    String remote;
    if (remoteName == null || remoteName.isEmpty())
      remote = "Remote.java";
    else
      remote = remoteName;

    if (!remote.startsWith("Remote"))
      remote = "Remote" + remote;
    if (!remote.endsWith(".java"))
      remote = remote + ".java";

    String path = "resources/langages/java/" + remote;

    InputStream stream = getClass().getClassLoader().getResourceAsStream(path);

    if (stream == null) {
      throw new IllegalArgumentException("Remote '" + path + "' do not exist (argument passed: '" + remoteName + "').");
    }

    String packageDeclaration = "package " + packageName + ";";
    String remoteCode         = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));

    if (remoteCode.startsWith("package")) {
      remoteCode = remoteCode.replaceFirst("package .*;", packageDeclaration);
    } else {
      remoteCode = packageDeclaration + "\n" + remoteCode;
    }

    return remoteCode;
  }

  private void copyFile(File name, String path, String packageName) throws IOException
  {
    String content = Files.readString(new File(path).toPath(), StandardCharsets.UTF_8);
    content        = content.replaceFirst("package .*;", "package " + packageName + ";\n");
    Files.writeString(name.toPath(), content);
  }

  public void compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {
    /* Make sure each run generate a new package to avoid that the loader cache prevent the reloading of the newly generated class */
    packageNameSuffix++;
    String packageNameCache = packageName();

    Map<String, String> runtimePatterns = new TreeMap<String, String>();
    runtimePatterns.put("\\$package", "package " + packageNameCache + ";");

    String mainRemoteContent =
        getRemoteJavaFile(null, packageNameCache).replace("import static ValueSerializer.*;", "import static " + packageNameCache + ".ValueSerializer.*;");
    ;

    DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<JavaFileObject>();
    try {
      for (SourceFile sf : exo.getSourceFilesList(this)) {
        String key = packageNameCache + "." + sf.getName();

        String correction = sf.getCorrection();

        String remote = getRemote(correction);
        if (remote == null) {
          PLMCompilerException e = new PLMCompilerException("This universe is not implemented in Java.", null, diagnostic);
          exo.lastResult         = RunOutcome.newCompilationError(e.getMessage());
          throw e;
        }

        String runFunction = extractRunFunction(correction);
        String dependency  = extractRunDependency(correction);
        String imports     = extractImportDependency(correction);

        runtimePatterns.put("\\$run", runFunction);
        runtimePatterns.put("\\$dependency", dependency);
        runtimePatterns.put("\\$imports", ("import static " + packageNameCache + ".ValueSerializer.*;\n"
                                           + "import java.awt.Color;\n"
                                           + "import static " + packageNameCache + ".Remote.*;\n"
                                           + "import static " + packageNameCache + "." + remote + ".*;\n" + imports)
                                              .replace('\n', ' '));

        String template = getCorrectedTemplate(correction);
        sf.setTemplate(template);

        String entityCode = sf.getCompilableContent(runtimePatterns, whatToCompile);
        entityCode        = Pattern.compile("([^a-zA-Z])(Direction)([^a-zA-Z.])").matcher(entityCode).replaceAll("$1int$3");
        entityCode        = Pattern.compile("this.").matcher(entityCode).replaceAll("");

        File workspace = new File(tempFolder, key.substring(0, key.lastIndexOf('.')).replace('.', '/'));
        // noinspection ResultOfMethodCallIgnored
        workspace.mkdirs();

        File mainRemote = new File(workspace, "Remote.java");

        String entityRemoteContent =
            getRemoteJavaFile(remote, packageNameCache).replace("import static Remote.*;", "import static " + packageNameCache + ".Remote.*;");
        File entityRemote = new File(workspace, remote + ".java");

        File entityFile = new File(workspace, "Entity.java");
        File mainFile   = new File(workspace, "Main.java");

        String mainContent = "package " + packageNameCache + ";\n"
                             + "import " + packageNameCache + ".Entity;\n"
                             + "import " + packageNameCache + ".Remote;\n"
                             + "\n"
                             + "public class Main {\n"
                             + "   public static void main(String[] args) {\n"
                             + "     try {\n"
                             + "       Remote.connect(args[0]);\n"
                             + "       new Entity().run();\n"
                             + "     } catch (Exception e) {\n"
                             + "       e.printStackTrace();\n"
                             + "       System.exit(1);\n"
                             + "     }\n"
                             + "     System.exit(0);\n"
                             + "   }\n"
                             + "}\n";

        try {
          File valueSerializer = new File(workspace, "ValueSerializer.java");
          copyFile(valueSerializer, "src/plm/core/ValueSerializer.java", packageNameCache);

          List<File> extraFiles = new ArrayList<>();
          for (String sourcePath : remoteExtraSourceFiles.getOrDefault(remote, List.of())) {
            File extraFile = new File(workspace, new File(sourcePath).getName());
            copyFile(extraFile, sourcePath, packageNameCache);
            extraFiles.add(extraFile);

            // Change the existing own source imports (e.g. "lessons.recursion.cons.universe.RecList") to the local one we just copied.
            String originalFqcn = fqcnFromSourcePath(sourcePath);
            String simpleName   = fileNameWithoutExtension(sourcePath);
            entityCode          = entityCode.replace("import " + originalFqcn + ";", "import " + packageNameCache + "." + simpleName + ";");
          }

          Files.writeString(new File(workspace, "Template.txt").toPath(), template);
          Files.writeString(new File(workspace, "Correction.txt").toPath(), correction);
          Files.writeString(mainRemote.toPath(), mainRemoteContent);
          Files.writeString(entityRemote.toPath(), entityRemoteContent);
          Files.writeString(entityFile.toPath(), entityCode);
          Files.writeString(mainFile.toPath(), mainContent);

          List<File> filesToCompile = new ArrayList<>(List.of(mainFile, mainRemote, entityRemote, entityFile, valueSerializer));
          filesToCompile.addAll(extraFiles);
          compileJavaFiles(diagnostic, workspace, filesToCompile.toArray(File[] ::new));

          File jarFile = new File(workspace, "Code.jar");
          List<File> filesToJar =
              new ArrayList<>(List.of(entityFile, mainRemote, entityRemote, valueSerializer, new File(workspace, "ValueSerializer$Parser.class")));
          filesToJar.addAll(extraFiles);
          createJarFile(diagnostic, tempFolder, workspace, jarFile, mainFile, filesToJar.toArray(File[] ::new));

          sf.meta.put("JAVA", jarFile.toPath().toString());

        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (PLMCompilerException e) {
      System.err.println(Game.i18n.tr("Compilation error:"));
      exo.lastResult = RunOutcome.newCompilationError(e.getDiagnostics());
      System.err.println(e.getMessage());
      if (out != null)
        out.log(exo.lastResult.compilationError); // display the same error as in the ExerciseFailedDialog

      if (Game.getInstance().isDebugEnabled())
        for (SourceFile sf : exo.getSourceFilesList(this))
          System.out.println("Source file " + sf.getName() + ":" + sf.getCompilableContent(runtimePatterns, whatToCompile));

      throw e;
    }
  }

  @Override public ArrayList<Entity> mutateEntities(Exercise exo, List<Entity> olds, StudentOrCorrection whatToMutate) throws PLMCompilerException
  {

    List<SourceFile> sourceFile = exo.getSourceFilesList(this);

    if (sourceFile.size() != 1)
      throw new IllegalStateException("ToBeYetImplemented: Cannot differentiate entity scripts for now.");

    SourceFile source = sourceFile.get(0);

    for (Entity old : olds) {
      String path = source.meta.get("JAVA");
      if (path != null) {
        old.setScript(this, path);
      }
    }

    return new ArrayList<>(olds);
  }

  @Override protected Entity mutateEntity(String newClassName) throws InstantiationException, IllegalAccessException
  {
    throw new RuntimeException("This function should not longer be called, the new implementation do not rely on it.");
  }

  @Override public void runEntity(final Entity ent, final RunOutcome progress)
  {
    final StringBuffer resEvaluationError = new StringBuffer();

    try {

      String executable;
      if (ent.getScript(this) != null) {
        executable = ent.getScript(this);
      } else {
        executable = Game.getInstance().getCurrentLesson().getCurrentExercise().getId();
        throw new IllegalStateException("TOFIX");
      }

      String cmd = executable;
      File exec  = new File(cmd);
      if (!exec.exists())
        throw new RuntimeException(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));

      // Set up the protocol socket (AF_UNIX) that the child JVM will connect to.
      // Its path is unique per run and is passed to the child as args[0].
      Path socketDir                    = Files.createTempDirectory("plm-java-sock-");
      Path socketPath                   = socketDir.resolve("protocol.sock");
      ServerSocketChannel serverChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
      serverChannel.bind(UnixDomainSocketAddress.of(socketPath));
      serverChannel.configureBlocking(false);
      Selector selector = Selector.open();
      serverChannel.register(selector, SelectionKey.OP_ACCEPT);

      ProcessBuilder pb     = new ProcessBuilder("java", "-jar", cmd, socketPath.toString());
      final Process process = pb.start();

      final int ACCEPT_TIMEOUT_MS = 10000;
      selector.select(ACCEPT_TIMEOUT_MS);
      SocketChannel protocolChannel = serverChannel.accept();
      selector.close();
      serverChannel.close();

      if (protocolChannel == null) {
        process.destroyForcibly();
        Files.deleteIfExists(socketPath);
        Files.deleteIfExists(socketDir);
        progress.outcome        = RunOutcome.kind.FAIL;
        progress.executionError = Game.i18n.tr("Protocol connection failed: the program never connected to the PLM.");
        return;
      }

      final SocketChannel finalProtocolChannel = protocolChannel;
      final BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(Channels.newOutputStream(finalProtocolChannel), StandardCharsets.UTF_8));

      Thread stdoutReader = new Thread() {
        public void run()
        {
          try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            try {
              String str;
              while ((str = reader.readLine()) != null)
                System.out.println(str);
            } finally {
              reader.close();
            }
          } catch (Throwable t) {
            t.printStackTrace();
            progress.outcome        = RunOutcome.kind.FAIL;
            progress.executionError = t.getMessage();
            process.destroyForcibly();
          }
        }
      };

      Thread stderrReader = new Thread() {
        public void run()
        {
          try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            try {
              String str;
              while ((str = reader.readLine()) != null)
                System.err.println(str);
            } finally {
              reader.close();
            }
          } catch (Throwable t) {
            t.printStackTrace();
          }
        }
      };

      Thread commandReader = new Thread() {
        public void run()
        {
          BufferedReader reader = new BufferedReader(new InputStreamReader(Channels.newInputStream(finalProtocolChannel), StandardCharsets.UTF_8));
          Exception parseError  = null;
          String str            = "";
          try {
            while ((str = reader.readLine()) != null) {
              //                      System.out.println("EXECUTING COMMAND: " + str);
              CommandExecutor.command(ent, str, bwriter);
              //                      System.out.println("COMMAND EXECUTED");
            }
          } catch (Exception e) {
            parseError = e;
            e.printStackTrace();
            progress.outcome        = RunOutcome.kind.FAIL;
            progress.executionError = e.getMessage();
            process.destroyForcibly();
          }
          if (parseError != null) {
            StringBuffer sb = new StringBuffer(str + "\n");
            try {
              while ((str = reader.readLine()) != null)
                sb.append(str + "\n");
            } catch (IOException ioe) {
              System.err.println("Exception while handling the exception. Bailing out");
              parseError.printStackTrace();
              ioe.printStackTrace();
            }
            throw new RuntimeException("Parse error while reading the command: " + sb.toString(), parseError);
          }
        }
      };

      stdoutReader.start();
      stderrReader.start();
      commandReader.start();

      int retcode = process.waitFor();

      stdoutReader.join();
      stderrReader.join();
      commandReader.join();

      bwriter.close();
      finalProtocolChannel.close();
      Files.deleteIfExists(socketPath);
      Files.deleteIfExists(socketDir);

      if (retcode != 0)
        progress.setExecutionError("An issue occured in the executed code. Check the output in the log panel for more info");

      if (resEvaluationError.length() > 0) {
        System.err.println(resEvaluationError.toString());
        progress.setCompilationError(resEvaluationError.toString());
      }

    } catch (Exception e) {
      resEvaluationError.append(e.getMessage());
      progress.setExecutionError(resEvaluationError.toString());
    }
  }

  public static class LangJavaExternalPrimitiveGenerator implements ExternalPrimitiveLanguage {

    String getLanguageType(Class<?> type)
    {
      if (type == Color.class)
        return "Color";
      if (type == Direction.class)
        return "int";
      if (type == Double.class || type == double.class)
        return "double";
      if (type == Integer.class || type == int.class)
        return "int";
      if (type == String.class)
        return "String";
      if (type == Character.class || type == char.class)
        return "char";
      if (type == Boolean.class || type == boolean.class)
        return "boolean";
      if (type == void.class || type == Void.class)
        return "void";

      throw new IllegalStateException("Unknown type: " + type);
    }

    String getTypeDeclaration(Class<?> type)
    {
      if (type == Direction.class) {
        return "public static class Direction {\n"
            + "\tstatic final int NORTH = 0;\n"
            + "\tstatic final int EAST = 1;\n"
            + "\tstatic final int SOUTH = 2;\n"
            + "\tstatic final int WEST = 3;\n"
            + "}";
      }
      return "";
    }

    String getParameter(PrimitiveParameter parameter) { return getLanguageType(parameter.type()) + " " + parameter.name(); }

    String getPrototype(PrimitiveMethod method)
    {
      String name                         = method.name();
      List<PrimitiveParameter> parameters = method.parameters();
      Class<?> output                     = method.output();

      final String outputString = Optional.ofNullable(output).map(this::getLanguageType).orElse("void");

      return "public static " + outputString + " " + name + "(" + parameters.stream().map(this::getParameter).collect(Collectors.joining(", ")) + ")";
    }

    String getReturning(Class<?> type)
    {
      if (type == null)
        return "";

      if (type == String.class)
        return "getAnswerString()";
      if (type == Double.class || type == double.class)
        return "getAnswerDouble()";
      if (type == Character.class || type == char.class)
        return "getAnswerChar()";
      if (type == Color.class)
        return "getAnswerColor()";
      if (type == Direction.class)
        return "getAnswerInt()";
      if (type == Integer.class || type == int.class)
        return "getAnswerInt()";
      if (type == Boolean.class || type == boolean.class)
        return "getAnswerBoolean()";
      if (type == void.class || type == Void.class)
        return "";

      throw new IllegalStateException("Unknown type: " + type);
    }

    String getArgumentExpression(PrimitiveParameter parameter)
    {
      // BOOLEAN is templated as "%d" over the wire, so we must convert any boolean to an int, or String.format will raise an error
      if (parameter.type() == Boolean.class || parameter.type() == boolean.class)
        return "(" + parameter.name() + " ? 1 : 0)";
      return parameter.name();
    }

    String getImplementation(PrimitiveMethod method)
    {
      String prototype = getPrototype(method);

      int id      = method.id();
      String name = method.name();

      String command = "\tsendCommand(\"" + id + "\", \"" + name + "\"" +
                       method.parameters().stream().map(this::getArgumentExpression).map(s -> ", " + s).collect(Collectors.joining()) + ");";

      String returning = method.hasReturn() ? "\treturn " + getReturning(method.output()) + ";" : "";

      return prototype + "{\n" + command + "\n" + returning + "\n}";
    }

    @Override public void generate(File folder, String name, List<PrimitiveMethod> methods) throws IOException { generate(folder, name, methods, ""); }

    @Override public void generate(File folder, String name, List<PrimitiveMethod> methods, String extraCode) throws IOException
    {
      Set<Class<?>> involved = ExternalPrimitiveLanguage.involved(methods);

      final String type_declarations = involved.stream().map(this::getTypeDeclaration).filter(o -> !o.isBlank()).collect(Collectors.joining("\n\n"));

      final String implementations = methods.stream().map(this::getImplementation).collect(Collectors.joining("\n\n"));

      String body = "\n" + type_declarations + "\n" + implementations;
      if (!extraCode.isBlank())
        body += "\n" + extraCode;

      final String code = "/* THIS FILE IS GENERATED. DO NOT EDIT */\nimport static Remote.*;\nimport java.awt.Color;\n\npublic class " + name + " {" +
                          body.replace("\n", "\n\t") + "\n}";

      System.err.println("XXX Generating " + folder + "/" + name + ".java");
      Files.writeString(new File(folder, name + ".java").toPath(), code);
    }
  }
}