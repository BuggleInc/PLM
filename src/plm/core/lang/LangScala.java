package plm.core.lang;

import java.awt.Color;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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
import plm.universe.Direction;
import plm.universe.Point;

/**
 * Remote execution for Scala, mirroring LangJava's architecture as closely as possible on purpose (see the class-level
 * comment there): an external "java" process is spawned per run, running compiled Scala bytecode that talks back to
 * CommandExecutor over a UNIX domain socket. Compilation is done by driving the SAME scala-compiler.jar the previously
 * embedded in-JVM compiler used (scala.tools.nsc.Main), but as a *separate* "java -cp <scala jars> scala.tools.nsc.Main"
 * process, to avoid assuming a standalone "scalac" binary is installed on the machine.
 *
 * Not yet factored with LangJava (structure kept close on purpose to make that factoring easy later); several private
 * helpers below are near-verbatim ports of LangJava's, adapted to Scala syntax where the generated code shape differs.
 */
public class LangScala extends JvmTemplatedLang {
  /**
   * Extra source files to be copied alongside the student's code
   */
  private static final Map<String, List<String>> remoteExtraSourceFiles = Map.of("RemoteCons", List.of("src/lessons/recursion/cons/universe/RecList.java"));
  /* Language detection logic */
  private static String brokenLanguageMessage;
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;
  File tempFolder                                        = TMP_ROOT.resolve("scala").toFile();

  public LangScala() { super("Scala", "scala", ResourcesCache.getIcon("img/lang_scala.png")); }
  @Override public boolean isScala() { return true; }

  @Override public String getBrokenLanguageMessage() { return brokenLanguageMessage; }
  @SuppressWarnings({"rawtypes", "unchecked"}) @Override public boolean isBrokenLanguage()
  {
    if (brokenLanguageState == BrokenLanguageState.Unitialized) {
      String[] resources = new String[] {"/scala/tools/nsc/Interpreter", "/scala/Unit", "/scala/reflect/io/AbstractFile"};
      String[] hints     = new String[] {"scala-compiler.jar", "scala-library.jar", "scala-reflect.jar"};
      for (int i = 0; i < resources.length; i++) {
        brokenLanguageMessage = canResolve(resources[i], hints[i]);
        if (!brokenLanguageMessage.isEmpty()) {
          System.err.println(brokenLanguageState);
          brokenLanguageState = BrokenLanguageState.NotUsable;
          return false;
        }
      }

      String version = "";
      try {
        Class props = Class.forName("scala.util.Properties");
        Method meth = props.getMethod("versionString", new Class[] {});
        version     = (String)meth.invoke(props);
      } catch (Exception e) {
        brokenLanguageMessage = Game.i18n.tr("Error {0} while retrieving the Scala version: {1}", e.getClass().getName(), e.getLocalizedMessage());
        System.err.println(brokenLanguageMessage);
        brokenLanguageState = BrokenLanguageState.NotUsable;
        return false;
      }

      if (version.contains("version 2.12") || version.contains("version 2.13")) {
        brokenLanguageState = BrokenLanguageState.Usable;
      } else {
        brokenLanguageMessage = Game.i18n.tr("Scala is too ancient. Found {0} while I need 2.12 or higher.", version);
        System.err.println(brokenLanguageMessage);
        brokenLanguageState = BrokenLanguageState.NotUsable;
        return false;
      }
    }
    return brokenLanguageState != BrokenLanguageState.Usable;
  }

  /**
   * e.g. "src/lessons/recursion/cons/universe/RecList.java" -> "lessons.recursion.cons.universe.RecList"
   */
  private static String fqcnFromSourcePath(String sourcePath)
  {
    String withoutSrcPrefix = sourcePath.startsWith("src/") ? sourcePath.substring("src/".length()) : sourcePath;
    String withoutExtension = withoutSrcPrefix.replaceFirst("\\.(java|scala)$", "");
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

  private static final String RUN_KEYWORD = "def run(";

  /**
   * Scala counterpart of LangJava's getCorrectedTemplate(): both now share their three-case logic and validation via
   * {@link JvmTemplatedLang#getCorrectedTemplate}, only the run() syntax and class/object wrapper differ.
   */
  public @NonNull String getCorrectedTemplate(String correction) throws PLMCompilerException
  {
    return getCorrectedTemplate(correction, RUN_KEYWORD, "def run(): Unit = { ... }", "package generated\n\n$imports\n\nobject Entity {\n",
                                "def run(): Unit = {");
  }

  private String extractRunFunction(String code) { return extractRunFunction(code, RUN_KEYWORD); }

  @Override public JvmExtraction extract(String correction, String template, String imports, String dependencies, String name) throws PLMCompilerException
  {
    return new JvmExtraction(getRemote(correction), extractRunFunction(correction), dependencies, imports, getCorrectedTemplate(correction),
                             deriveCorrectionBody(correction, name));
  }

  /**
   * Absolute path of the jar a given class was loaded from -- used to locate scala-library.jar/scala-compiler.jar/
   * scala-reflect.jar on disk (already proven present as PLM dependencies by isBrokenLanguage() above), so the external
   * "java" processes below can be given an explicit classpath without assuming any standalone "scalac"/"scala" binary is
   * installed on the machine.
   */
  private static String jarPathFor(Class<?> cls)
  {
    try {
      return new File(cls.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath();
    } catch (Exception e) {
      throw new RuntimeException("Cannot locate the jar providing " + cls.getName() + ". Is Scala properly installed?", e);
    }
  }

  private static String scalaCompilerClasspath()
  {
    return String.join(File.pathSeparator, jarPathFor(scala.tools.nsc.Main.class), jarPathFor(scala.collection.immutable.List.class),
                       jarPathFor(scala.reflect.io.AbstractFile.class));
  }

  private static String scalaLibraryJar() { return jarPathFor(scala.collection.immutable.List.class); }

  private static void compileScalaFiles(DiagnosticCollector<JavaFileObject> diagnostic, File packageFolder, File... files) throws PLMCompilerException
  {
    List<File> javaFiles  = Arrays.asList(files).stream().filter(f -> f.getName().endsWith(".java")).toList();
    List<File> scalaFiles = Arrays.asList(files).stream().filter(f -> f.getName().endsWith(".scala")).toList();
    List<String> paths    = Arrays.asList(files).stream().map(s -> s.toPath().toString()).toList();

    if (!javaFiles.isEmpty()) {
      ArrayList<String> javacArgs = new ArrayList<>();
      javacArgs.add("javac");
      javacArgs.add("-d");
      javacArgs.add(".");
      javaFiles.forEach(f -> javacArgs.add(f.getName()));
      runToolProcess(diagnostic, packageFolder, paths, javacArgs);
    }

    if (!scalaFiles.isEmpty()) {
      ArrayList<String> scalacArgs = new ArrayList<>();
      scalacArgs.add("java");
      scalacArgs.add("-cp");
      scalacArgs.add(scalaCompilerClasspath());
      scalacArgs.add("scala.tools.nsc.Main");
      scalacArgs.add("-classpath");
      // "." : packageFolder itself, where javac (above, if any) just wrote the already-compiled Java classes.
      scalacArgs.add(scalaCompilerClasspath() + File.pathSeparator + ".");
      scalacArgs.add("-d");
      scalacArgs.add(".");
      scalaFiles.forEach(f -> scalacArgs.add(f.getName()));
      runToolProcess(diagnostic, packageFolder, paths, scalacArgs);
    }
  }

  private static void runToolProcess(DiagnosticCollector<JavaFileObject> diagnostic, File packageFolder, List<String> paths, List<String> args)
      throws PLMCompilerException
  {
    try {
      String[] params = args.toArray(String[] ::new);
      Process proc    = Runtime.getRuntime().exec(params, new String[] {}, packageFolder);

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

      String rtStdout = stdInput.lines().collect(Collectors.joining("\n"));
      String rtStderr = stdError.lines().collect(Collectors.joining("\n"));

      int retcode = -1;
      try {
        retcode = proc.waitFor();
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
      }

      if (retcode != 0 || !rtStderr.isBlank()) {
        String msg = "The following command failed: " + String.join(" ", params);
        msg += rtStderr.isBlank() ? rtStdout : rtStderr;

        throw new PLMCompilerException(msg.toString(), new HashSet<>(paths), new Error(), diagnostic);
      }
    } catch (IOException e) {
      throw new PLMCompilerException(e.getMessage(), new HashSet<>(paths), new Error(), diagnostic);
    }
  }

  /**
   * Builds a runnable jar for the compiled classes, with a Class-Path manifest entry pointing at scala-library.jar so
   * "java -jar" alone (no external -cp needed at run time, mirroring LangJava's runEntity() as closely as possible) can
   * run compiled Scala bytecode.
   */
  private static void createJarFile(DiagnosticCollector<JavaFileObject> diagnostic, File packageFolder, File jarFile, String mainClassDotPath,
                                    Set<String> classFiles) throws PLMCompilerException
  {
    runJarTool(packageFolder, jarFile, mainClassDotPath, classFiles, diagnostic);
  }

  public String getRemoteScalaFile(String remoteName)
  {
    String remoteCode         = loadRemoteFile(remoteName, "scala", ".scala");
    String packageDeclaration = "package generated";

    if (remoteCode.startsWith("package"))
      remoteCode = remoteCode.replaceFirst("package .*", packageDeclaration);
    else
      remoteCode = packageDeclaration + "\n" + remoteCode;

    return remoteCode;
  }

  private String copyFile(String path) throws IOException
  {
    String content = Files.readString(new File(path).toPath(), StandardCharsets.UTF_8);
    // Java source files copied in verbatim (e.g. RecList.java, ValueSerializer.java, Point.java) use "package x.y.z;",
    // Scala's own generated files use "package x.y.z" (no semicolon) -- replaceFirst matches either.
    content = content.replaceFirst("package [^;\\n]*;?", "package generated" + (path.endsWith(".java") ? ";" : ""));
    return content;
  }

  @Override public String compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {
    String packageNameCache = packageNameForExercise(exo, whatToCompile);

    Map<String, String> runtimePatterns = new TreeMap<String, String>();

    String mainRemoteContent = getRemoteScalaFile(null);

    String jarPath                                 = null;
    DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<JavaFileObject>();
    try {
      for (SourceFile sf : exo.getSourceFilesList(this)) {
        String key = packageNameCache + "." + sf.getName();

        JvmExtraction extraction   = (JvmExtraction)sf.getExtraction();
        String remote              = checkRemoteOrFail(extraction.remote(), "Scala", exo, diagnostic);

        runtimePatterns.put("\\$run", extraction.runFunction());
        runtimePatterns.put("\\$dependency", extraction.dependency());
        runtimePatterns.put("\\$imports", ("import generated.ValueSerializer._; "
                                           + "import java.awt.Color; "
                                           + "import generated.Remote._; "
                                           + "import generated." + remote + "._; " + extraction.rawImports())
                                              .replace('\n', ' '));

        String entityCode = sf.getCompilableContent(runtimePatterns, whatToCompile).content();
        entityCode        = Pattern.compile("([^a-zA-Z])(Direction)([^a-zA-Z.])").matcher(entityCode).replaceAll("$1Int$3");
        entityCode        = Pattern.compile("this\\.").matcher(entityCode).replaceAll("");
        // Scala's "override" needs a real supertype member to override, but Entity is a flat `object` extending nothing
        // so we strip "override"s just as LangJava strips "@Override" there for the exact same reason.
        entityCode = Pattern.compile("\\boverride\\b").matcher(entityCode).replaceAll("");

        File workspace = new File(tempFolder, key.substring(0, key.lastIndexOf('.')).replace('.', '/'));
        workspace.mkdirs();

        File mainRemote = new File(workspace, "Remote.scala");

        String entityRemoteContent = getRemoteScalaFile(remote);
        File entityRemote          = new File(workspace, remote + ".scala");

        File entityFile = new File(workspace, "Entity.scala");
        File mainFile   = new File(workspace, "Main.scala");

        String mainContent = "package generated\n"
                             + "\n"
                             + "object Main {\n"
                             + "  def main(args: Array[String]): Unit = {\n"
                             + "    try {\n"
                             + "      Remote.connect(args(0))\n"
                             + "      Entity.run()\n"
                             + "    } catch {\n"
                             + "      case e: Exception =>\n"
                             + "        e.printStackTrace()\n"
                             + "        System.exit(1)\n"
                             + "    }\n"
                             + "    System.exit(0)\n"
                             + "  }\n"
                             + "}\n";

        try {
          File valueSerializer = new File(workspace, "ValueSerializer.java");

          List<String> extraSourcePaths = new ArrayList<>(List.of("src/plm/universe/Point.java"));
          extraSourcePaths.addAll(remoteExtraSourceFiles.getOrDefault(remote, List.of()));

          // See LangJava.compileExo()'s identical comment: same reasoning, same fix, applied here too.
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

          Files.writeString(new File(workspace, "Template.txt").toPath(), extraction.template());
          Files.writeString(new File(workspace, "Correction.txt").toPath(), extraction.correctionBody());
          Files.writeString(mainRemote.toPath(), mainRemoteContent);
          Files.writeString(entityRemote.toPath(), entityRemoteContent);
          Files.writeString(entityFile.toPath(), entityCode);
          Files.writeString(mainFile.toPath(), mainContent);

          List<File> filesToCompile = new ArrayList<>(List.of(mainFile, mainRemote, entityRemote, entityFile, valueSerializer));
          filesToCompile.addAll(extraFiles);
          compileScalaFiles(diagnostic, workspace, filesToCompile.toArray(File[] ::new));

          // Scala compiles "object Main" to Main.class (plus a Main$.class holding the singleton); the manifest only
          // needs the former as its Main-Class entry point, exactly like a Java class with a static main().
          File jarFile = new File(workspace, "Code.jar");

          // Relative to workspace itself (where scalac actually wrote these under their package-name subdirectories),
          // not to its parent: this is what must end up as each entry's name inside the jar.
          // For example "plm/runtime4/Main.class" needs "-cp jarfile plm.runtime4.Main" to resolve.
          Set<String> classFiles = new HashSet<>();
          try (var walk = Files.walk(workspace.toPath())) {
            walk.filter(p -> p.toString().endsWith(".class")).forEach(p -> classFiles.add(workspace.toPath().relativize(p).toString()));
          }

          createJarFile(diagnostic, workspace, jarFile, "generated.Main", classFiles);

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
        out.log(exo.lastResult.compilationError);

      if (Game.getInstance().isDebugEnabled())
        for (SourceFile sf : exo.getSourceFilesList(this))
          System.out.println("Source file " + sf.getName() + ":" + sf.getCompilableContent(runtimePatterns, whatToCompile).content());

      throw e;
    }
    return jarPath;
  }

  /**
   * Runs "java -cp &lt;jarPath&gt;:&lt;scala-library.jar&gt; generated.Main &lt;socketPath&gt;", executable being the jar path
   * returned by compileExo() -- the main class is always "generated.Main". We cannot use "java -jar" alone because a jar's
   * Class-Path manifest attribute is only reliably resolved for relative paths, while the path of scala-library.jar is probably
   * absolute, leading to silent failures at startup.
   */
  @Override protected ProcessBuilder buildProcess(String executable, Path socketPath) throws IOException
  {
    File exec = new File(executable);
    if (!exec.exists())
      throw new RuntimeException(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));

    return new ProcessBuilder("java", "-cp", executable + File.pathSeparator + scalaLibraryJar(), "generated.Main", socketPath.toString());
  }

  public static class LangScalaExternalPrimitiveGenerator extends JvmExternalPrimitiveGenerator {

    String getLanguageType(Class<?> type)
    {
      if (type == Color.class)
        return "Color";
      if (type == Direction.class)
        return "Int";
      if (type == Point.class)
        return "Point";
      if (type == Point[].class)
        return "Array[Point]";
      if (type == Double.class || type == double.class)
        return "Double";
      if (type == Integer.class || type == int.class)
        return "Int";
      if (type == String.class)
        return "String";
      if (type == Character.class || type == char.class)
        return "Char";
      if (type == Boolean.class || type == boolean.class)
        return "Boolean";
      if (type == void.class || type == Void.class)
        return "Unit";

      throw new IllegalStateException("Unknown type: " + type);
    }

    String getTypeDeclaration(Class<?> type)
    {
      if (type == Direction.class) {
        return "object Direction {\n"
            + "\tval NORTH = 0\n"
            + "\tval EAST = 1\n"
            + "\tval SOUTH = 2\n"
            + "\tval WEST = 3\n"
            + "}";
      }
      return "";
    }

    String getParameter(PrimitiveParameter parameter) { return parameter.name() + ": " + getLanguageType(parameter.type()); }

    String getPrototype(PrimitiveMethod method)
    {
      String name                         = method.name();
      List<PrimitiveParameter> parameters = method.parameters();
      Class<?> output                     = method.output();

      final String outputString = Optional.ofNullable(output).map(this::getLanguageType).orElse("Unit");

      return "def " + name + "(" + parameters.stream().map(this::getParameter).collect(Collectors.joining(", ")) + "): " + outputString;
    }

    String getImplementation(PrimitiveMethod method)
    {
      String prototype = getPrototype(method);

      int id      = method.id();
      String name = method.name();

      String command = "\tsendCommand(\"" + id + "\", \"" + name + "\"" +
                       method.parameters().stream().map(PrimitiveParameter::name).map(s -> ", " + s + ".asInstanceOf[Object]").collect(Collectors.joining()) +
                       ")";

      String returning = method.hasReturn() ? "\t" + getReturning(method.output()) : "";

      return prototype + " = {\n" + command + "\n" + returning + "\n}";
    }

    String fileExtension() { return ".scala"; }

    String wrapCode(String name, String body)
    {
      return "/* THIS FILE IS GENERATED. DO NOT EDIT */\nimport Remote._\nimport java.awt.Color\n\nobject " + name + " {" + body.replace("\n", "\n\t") + "\n}";
    }
  }
}
