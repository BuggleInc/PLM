package plm.core.lang;

import java.awt.*;
import java.io.*;
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
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.session.SourceFile;
import plm.core.ui.ResourcesCache;
import plm.universe.Direction;
import plm.universe.Point;

public class LangJava extends JvmTemplatedLang {
  /**
   * Extra source files to be copied alongside the student's code
   */
  private static final Map<String, List<String>> remoteExtraSourceFiles = Map.of("RemoteCons", List.of("src/lessons/recursion/cons/universe/RecList.java"));
  /* Language detection logic */
  private static String brokenLanguageMessage;
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;
  File tempFolder                                        = TMP_ROOT.resolve("java").toFile();

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

  private static final String RUN_KEYWORD = "void run(";

  private static @NonNull String getCorrectedTemplate(String correction)
  {
    int beginTemplateIndex    = correction.indexOf("/* BEGIN TEMPLATE */");
    int beginTemplateIndexEnd = beginTemplateIndex + "/* BEGIN TEMPLATE */".length();
    int endTemplateIndex      = correction.indexOf("/* END TEMPLATE */");
    int endTemplateIndexEnd   = endTemplateIndex + "/* END TEMPLATE */".length();
    int runFunctionI          = correction.indexOf(RUN_KEYWORD);

    // Containment between the templated region [beginTemplateIndexRaw, endTemplateIndexEnd) and run() body
    int[] runSpan = ExerciseTemplated.extractRunSpan(correction, RUN_KEYWORD);

    String template;
    if (runSpan != null && runSpan[0] <= runFunctionI && endTemplateIndex != -1 && beginTemplateIndex <= runFunctionI && runFunctionI <= endTemplateIndex) {
      // run()'s own declaration falls inside the templated region: the templated text IS run() (signature included).
      template = "$package\n\n$imports\n\npublic class Entity {\n$dependency\n\t\n$body\n}";
    } else if (runSpan != null && runSpan[0] <= beginTemplateIndex && endTemplateIndexEnd <= runSpan[1]) {
      // The templated region sits fully inside run()'s braces, but run()'s own declaration line is outside it: the
      // templated text is just run()'s body.
      template = "$package\n\n$imports\n\npublic class Entity {\n$dependency\n\tpublic void run(){\n$body\t}\n}";
    } else {
      // run() and the templated region are disjoint (e.g. templated code lives in a separate step()-like method): keep
      // run() intact via $run, and place the templated text elsewhere in the class body via $body.
      template = "$package\n\n$imports\n\npublic class Entity {\n$dependency\n$run\n\t\n$body\n}";
    }
    return template;
  }

  private static String extractRunFunction(String code) { return ExerciseTemplated.extractRunFunction(code, RUN_KEYWORD); }

  private static String extractRunDependency(String code) { return extractMarkedSection(code, "/* BEGIN DEPENDENCY */", "/* END DEPENDENCY */"); }

  private static String extractImportDependency(String code) { return extractMarkedSection(code, "/* BEGIN IMPORT */", "/* END IMPORT */"); }

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

    if (!packageFolder.toPath().toString().startsWith(root.toPath().toString())) {
      throw new PLMCompilerException("Root folder (" + root.toPath() + ") is not above package folder (" + packageFolder.toPath() + ") in file hierarchy.",
                                     Set.of(), new Error(), diagnostic);
    }

    String packagePath = packageFolder.toPath().toString().substring(root.toPath().toString().length() + 1);
    String packageName = packagePath.replace('/', '.');

    String mainFileDotPath = packageName + "." + mainFile.getName().substring(0, mainFile.getName().indexOf('.'));

    List<String> classFiles = allFiles.stream()
                                  .map(s -> {
                                    String javaPath = s.toPath().toString();
                                    return javaPath.substring(0, javaPath.lastIndexOf('.')) + ".class";
                                  })
                                  .filter(s -> s.endsWith(".class"))
                                  .toList();
    classFiles = classFiles.stream().map(s -> s.substring(root.toPath().toString().length() + 1)).toList();

    runJarTool(root, jarFile, mainFileDotPath, new HashSet<>(classFiles), diagnostic);
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
    String remoteCode         = loadRemoteFile(remoteName, "java", ".java");
    String packageDeclaration = "package " + packageName + ";";

    if (remoteCode.startsWith("package"))
      remoteCode = remoteCode.replaceFirst("package .*;", packageDeclaration);
    else
      remoteCode = packageDeclaration + "\n" + remoteCode;

    return remoteCode;
  }

  private String copyFile(String path, String packageName) throws IOException
  {
    String content = Files.readString(new File(path).toPath(), StandardCharsets.UTF_8);
    content        = content.replaceFirst("package .*;", "package " + packageName + ";\n");
    return content;
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

        String remote = getRemoteOrFail(correction, "Java", exo, diagnostic);

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
        entityCode        = Pattern.compile("@Override").matcher(entityCode).replaceAll("");

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

          List<String> extraSourcePaths = new ArrayList<>(Arrays.asList("src/plm/universe/Point.java", "src/plm/core/ValueSerializer.java"));
          extraSourcePaths.addAll(remoteExtraSourceFiles.getOrDefault(remote, List.of()));

          // Rewrite any import of a type we're about to copy locally, in EVERY file that might reference it (the
          // student's own code, and our own runtime files like ValueSerializer.java) -- otherwise e.g.
          // ValueSerializer.deserialize() would keep building instances of the ORIGINAL plm.universe.Point while
          // RemoteLander.java expects the local copy: same simple name, different package, so it compiles fine and throws
          // ClassCastException at runtime.
          java.util.function.UnaryOperator<String> rewriteExtraImports = content ->
          {
            for (String sourcePath : extraSourcePaths) {
              String originalFqcn = fqcnFromSourcePath(sourcePath);
              String simpleName   = fileNameWithoutExtension(sourcePath);
              content             = content.replace("import " + originalFqcn + ";", "import " + packageNameCache + "." + simpleName + ";");
            }
            return content;
          };

          entityCode = rewriteExtraImports.apply(entityCode);
          Files.writeString(valueSerializer.toPath(), rewriteExtraImports.apply(copyFile("src/plm/core/ValueSerializer.java", packageNameCache)));

          List<File> extraFiles = new ArrayList<>();
          for (String sourcePath : extraSourcePaths) {
            File extraFile = new File(workspace, new File(sourcePath).getName());
            Files.writeString(extraFile.toPath(), rewriteExtraImports.apply(copyFile(sourcePath, packageNameCache)));
            extraFiles.add(extraFile);
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

  /** Runs "java -jar &lt;executable&gt; &lt;socketPath&gt;", the executable being the jar path produced by compileExo(). */
  @Override protected ProcessBuilder buildProcess(String executable, Path socketPath) throws IOException
  {
    File exec = new File(executable);
    if (!exec.exists())
      throw new RuntimeException(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));

    return new ProcessBuilder("java", "-jar", executable, socketPath.toString());
  }

  public static class LangJavaExternalPrimitiveGenerator extends JvmExternalPrimitiveGenerator {

    String getLanguageType(Class<?> type)
    {
      if (type == Color.class)
        return "Color";
      if (type == Direction.class)
        return "int";
      if (type == Point.class)
        return "Point";
      if (type == Point[].class)
        return "Point[]";
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

    String fileExtension() { return ".java"; }

    String wrapCode(String name, String body)
    {
      return "/* THIS FILE IS GENERATED. DO NOT EDIT */\nimport static Remote.*;\nimport java.awt.Color;\n\npublic class " + name + " {" +
          body.replace("\n", "\n\t") + "\n}";
    }
  }
}