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
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
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

  private static void compileJavaFiles(DiagnosticCollector<JavaFileObject> diagnostic, File classOutputDir, File... files) throws PLMCompilerException
  {
    for (File javaFile : files) {

      String path = javaFile.toPath().toString();
      if (!path.endsWith(".java")) {
        throw new PLMCompilerException("Trying to compile a non java file: '" + path + "'", Set.of(path), new Error(), diagnostic);
      }
    }

    List<String> paths = Arrays.asList(files).stream().map(s -> s.toPath().toString()).toList();

    // Do not start an external javac process that takes time to kick in, but use javax.tools.JavaCompiler (the API "javac" itself is built on)
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null)
      throw new PLMCompilerException("No system Java compiler available: PLM must run on a JDK, not a JRE.", new HashSet<>(paths), new Error(), diagnostic);

    try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostic, null, StandardCharsets.UTF_8)) {
      // set CLASS_PATH="" (empty) instead of java.class.path, so that the student code cannot see PLM's own classes.
      // CLASS_OUTPUT is classOutputDir itself, so ".class" files land nested under their "generated/" package folder.
      fileManager.setLocation(StandardLocation.CLASS_PATH, List.of());
      fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(classOutputDir));

      Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
      boolean success                                     = compiler.getTask(null, fileManager, diagnostic, null, null, compilationUnits).call();

      // Any diagnostic, even a warning, is treated as an error.
      if (!success || !diagnostic.getDiagnostics().isEmpty()) {
        String rtStderr = diagnostic.getDiagnostics().stream().map(Object::toString).collect(Collectors.joining("\n"));
        throw new PLMCompilerException(rtStderr, new HashSet<>(paths), new Error(), diagnostic);
      }
    } catch (IOException e) {
      throw new PLMCompilerException(e.getMessage(), new HashSet<>(paths), new Error(), diagnostic);
    }
  }

  private static void createJarFile(DiagnosticCollector<JavaFileObject> diagnostic, File root, File packageFolder, File jarFile, File mainFile)
      throws PLMCompilerException
  {

    if (!packageFolder.toPath().toString().startsWith(root.toPath().toString())) {
      throw new PLMCompilerException("Root folder (" + root.toPath() + ") is not above package folder (" + packageFolder.toPath() + ") in file hierarchy.",
                                     Set.of(), new Error(), diagnostic);
    }

    // Every class compiles under the fixed "generated" package (see compileJavaFiles(), which sets CLASS_OUTPUT to
    // packageFolder itself), so ".class" files land nested under a "generated/" folder there, same as any normal -d
    // compile -- walk packageFolder for them rather than deriving their location from the ".java" source files,
    // which no longer tells us where javac put the output.
    String mainFileDotPath = "generated." + mainFile.getName().substring(0, mainFile.getName().indexOf('.'));

    List<String> classFiles;
    try (var paths = Files.walk(packageFolder.toPath())) {
      classFiles = paths.filter(p -> p.toString().endsWith(".class")).map(p -> packageFolder.toPath().relativize(p).toString()).toList();
    } catch (IOException e) {
      throw new PLMCompilerException(e.getMessage(), Set.of(), new Error(), diagnostic);
    }

    runJarTool(packageFolder, jarFile, mainFileDotPath, new HashSet<>(classFiles), diagnostic);
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

  public String getRemoteJavaFile(String remoteName)
  {
    String remoteCode         = loadRemoteFile(remoteName, "java", ".java");
    String packageDeclaration = "package generated;";

    if (remoteCode.startsWith("package"))
      remoteCode = remoteCode.replaceFirst("package .*;", packageDeclaration);
    else
      remoteCode = packageDeclaration + "\n" + remoteCode;

    return remoteCode;
  }

  private String copyFile(String path) throws IOException
  {
    String content = Files.readString(new File(path).toPath(), StandardCharsets.UTF_8);
    content        = content.replaceFirst("package .*;", "package generated;\n");
    return content;
  }

  public String compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {
    String packageNameCache = packageNameForExercise(exo, whatToCompile);

    Map<String, String> runtimePatterns = new TreeMap<String, String>();

    String mainRemoteContent = getRemoteJavaFile(null);

    String jarPath                                 = null;
    DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<JavaFileObject>();
    List<String> generatedSources = new ArrayList<>(); // kept to dump them on failure when debugging
    try {
      for (SourceFile sf : exo.getSourceFilesList(this)) {
        String key = packageNameCache + "." + sf.getName();

        String remote = checkRemoteOrFail(sf.getRemote(), "Java", exo, diagnostic);

        runtimePatterns.put("\\$imports", ("import static generated.ValueSerializer.*;\n"
                                           + "import java.awt.Color;\n"
                                           + "import static generated.Remote.*;\n"
                                           + "import static generated." + remote + ".*;\n" + sf.getImports())
                                              .replace('\n', ' '));

        String template   = getCorrectedTemplate("package generated;\n\n$imports\n\npublic class Entity {\n",
                                               whatToCompile == StudentOrCorrection.CORRECTION ? sf.getCorrectionTemplate() : sf.getTemplate());
        String entityCode = sf.getCompilableContent(template, runtimePatterns, whatToCompile).content();
        generatedSources.add(sf.getName() + ":" + entityCode);
        entityCode        = Pattern.compile("([^a-zA-Z])(Direction)([^a-zA-Z.])").matcher(entityCode).replaceAll("$1int$3");
        entityCode        = Pattern.compile("this.").matcher(entityCode).replaceAll("");
        entityCode        = Pattern.compile("@Override").matcher(entityCode).replaceAll("");

        File workspace = new File(tempFolder, key.substring(0, key.lastIndexOf('.')).replace('.', '/'));
        // noinspection ResultOfMethodCallIgnored
        workspace.mkdirs();

        File mainRemote = new File(workspace, "Remote.java");

        String entityRemoteContent = getRemoteJavaFile(remote);
        File entityRemote          = new File(workspace, remote + ".java");

        File entityFile = new File(workspace, "Entity.java");
        File mainFile   = new File(workspace, "Main.java");

        String mainContent = "package generated;\n"
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
              content             = content.replace("import " + originalFqcn + ";", "import generated." + simpleName + ";");
            }
            return content;
          };

          entityCode = rewriteExtraImports.apply(entityCode);
          Files.writeString(valueSerializer.toPath(), rewriteExtraImports.apply(copyFile("src/plm/core/ValueSerializer.java")));

          List<File> extraFiles = new ArrayList<>();
          for (String sourcePath : extraSourcePaths) {
            File extraFile = new File(workspace, new File(sourcePath).getName());
            Files.writeString(extraFile.toPath(), rewriteExtraImports.apply(copyFile(sourcePath)));
            extraFiles.add(extraFile);
          }

          Files.writeString(mainRemote.toPath(), mainRemoteContent);
          Files.writeString(entityRemote.toPath(), entityRemoteContent);
          Files.writeString(entityFile.toPath(), entityCode);
          Files.writeString(mainFile.toPath(), mainContent);

          List<File> filesToCompile = new ArrayList<>(List.of(mainFile, mainRemote, entityRemote, entityFile, valueSerializer));
          filesToCompile.addAll(extraFiles);
          compileJavaFiles(diagnostic, workspace, filesToCompile.toArray(File[] ::new));

          File jarFile = new File(workspace, "Code.jar");
          createJarFile(diagnostic, tempFolder, workspace, jarFile, mainFile);

          jarPath = jarFile.toPath().toString();

        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (PLMCompilerException e) {
      System.err.println(Game.i18n.tr("Compilation error:"));
      exo.lastResult = RunOutcome.newCompilationError(e.getMessage());
      System.err.println(e.getMessage());
      if (out != null)
        out.log(exo.lastResult.compilationError); // display the same error as in the ExerciseFailedDialog

      if (Game.getInstance().isDebugEnabled())
        for (String source : generatedSources)
          System.out.println("Source file " + source);

      throw e;
    }
    return jarPath;
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

    String getImplementation(PrimitiveMethod method)
    {
      String prototype = getPrototype(method);

      int id      = method.id();
      String name = method.name();

      String command = "\tsendCommand(\"" + id + "\", \"" + name + "\"" +
                       method.parameters().stream().map(PrimitiveParameter::name).map(s -> ", " + s).collect(Collectors.joining()) + ");";

      String returning = method.hasReturn() ? "\treturn " + getReturning(method.output()) + ";" : "";

      return prototype + "{\n" + command + "\n" + returning + "\n}";
    }

    String fileExtension() { return ".java"; }

    String wrapCode(String name, String body)
    {
      return "/* THIS FILE IS GENERATED. DO NOT EDIT */\nimport static generated.Remote.*;\nimport java.awt.Color;\n\npublic class " + name + " {" +
          body.replace("\n", "\n\t") + "\n}";
    }
  }
}