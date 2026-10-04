package plm.core.lang;

import dotty.tools.dotc.Driver;
import dotty.tools.dotc.reporting.Reporter;
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
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import plm.core.PLMCompilerException;
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
 * Remote execution for Scala, mirroring LangJava's architecture as closely as possible on purpose (see the class-level comment
 * there): an external "java" process is spawned per run, running compiled Scala bytecode that talks back to CommandExecutor
 * over a UNIX domain socket. Compilation happens entirely in-process, exactly like LangJava: the ancillary ".java" helper files
 * copied alongside the student's Scala code (ValueSerializer.java, Point.java, RecList.java) go through
 * javax.tools.JavaCompiler, and the Scala 3 compiler shipped as a PLM dependency is driven directly through
 * dotty.tools.dotc.Driver.
 *
 * The structure is kept close to LangJava's on purpose, even if we favor code readability and flow linearity over absolute code
 * factorization.
 */
public class LangScala extends JvmTemplatedLang {
  /* Language detection logic */
  private static String brokenLanguageMessage;
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;

  public LangScala() { super("Scala", "scala", ResourcesCache.getIcon("img/lang_scala.png")); }
  @Override public boolean isScala() { return true; }

  @Override public String getBrokenLanguageMessage() { return brokenLanguageMessage; }
  @SuppressWarnings({"rawtypes", "unchecked"}) @Override public boolean isBrokenLanguage()
  {
    if (brokenLanguageState == BrokenLanguageState.Unitialized) {
      String[] resources = new String[] {"/dotty/tools/dotc/Main", "/scala/Unit", "/scala/runtime/LazyVals$"};
      String[] hints     = new String[] {"scala3-compiler.jar", "scala-library.jar", "scala3-library.jar"};
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
        // dotty.tools.dotc.config.Properties is a Scala object: read its singleton instance.
        Class props = Class.forName("dotty.tools.dotc.config.Properties$");
        Method meth = props.getMethod("simpleVersionString");
        version     = (String)meth.invoke(props.getField("MODULE$").get(null));
      } catch (Exception e) {
        brokenLanguageMessage = Game.i18n.tr("Error {0} while retrieving the Scala version: {1}", e.getClass().getName(), e.getLocalizedMessage());
        System.err.println(brokenLanguageMessage);
        brokenLanguageState = BrokenLanguageState.NotUsable;
        return false;
      }

      if (version.startsWith("3.")) {
        brokenLanguageState = BrokenLanguageState.Usable;
      } else {
        brokenLanguageMessage = Game.i18n.tr("Unsupported Scala version. Found {0} while I need Scala 3.", version);
        System.err.println(brokenLanguageMessage);
        brokenLanguageState = BrokenLanguageState.NotUsable;
        return false;
      }
    }
    return brokenLanguageState != BrokenLanguageState.Usable;
  }

  /** Classes identifying the jars that the Scala compiler needs on its own classpath (the first four are mandatory). */
  private static final String[] SCALA_COMPILER_CLASSES = {"dotty.tools.dotc.Main", "dotty.tools.tasty.TastyFormat", "dotty.tools.dotc.interfaces.Diagnostic",
                                                          "scala.tools.asm.ClassWriter"};
  /** Jars only needed by some compiler code paths; silently skipped when absent. */
  private static final String[] SCALA_COMPILER_OPTIONAL_CLASSES = {"xsbti.Reporter"};
  /** Classes identifying the jars that compiled Scala code needs at run time. */
  private static final String[] SCALA_RUNTIME_CLASSES = {"scala.collection.immutable.List", "scala.runtime.LazyVals$"};

  /**
   * Absolute path of the jar a given class was loaded from, or null if the class cannot be found. This locates the Scala
   * jars on disk (proven present as PLM dependencies by isBrokenLanguage() above), so the external "java" processes below get
   * an explicit classpath without assuming any standalone "scalac"/"scala" binary is installed. Classes are not initialized.
   */
  private static String jarPathFor(String className)
  {
    try {
      Class<?> cls = Class.forName(className, false, LangScala.class.getClassLoader());
      return new File(cls.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath();
    } catch (ClassNotFoundException e) {
      return null;
    } catch (Exception e) {
      throw new RuntimeException("Cannot locate the jar providing " + className + ". Is Scala properly installed?", e);
    }
  }

  /** Path-separated, duplicate-free classpath of the jars providing the given classes. Fails on a missing class if mandatory. */
  private static String classpathOf(boolean mandatory, String... classNames)
  {
    List<String> jars = new ArrayList<>();
    for (String name : classNames) {
      String jar = jarPathFor(name);
      if (jar == null && mandatory)
        throw new RuntimeException("Cannot locate the jar providing " + name + ". Is Scala properly installed?");
      if (jar != null && !jars.contains(jar))
        jars.add(jar);
    }
    return String.join(File.pathSeparator, jars);
  }

  private static String scalaCompilerClasspath()
  {
    String optional = classpathOf(false, SCALA_COMPILER_OPTIONAL_CLASSES);
    return classpathOf(true, SCALA_COMPILER_CLASSES) + File.pathSeparator + scalaRuntimeClasspath() + (optional.isEmpty() ? "" : File.pathSeparator + optional);
  }

  private static String scalaRuntimeClasspath() { return classpathOf(true, SCALA_RUNTIME_CLASSES); }

  /**
   * Compiles the plain ".java" helper files copied alongside the student's Scala code (ValueSerializer.java,
   * Point.java, RecList.java) in-process, exactly like LangJava.compileJavaFiles() does for the student's own Java
   * entities. classOutputDir is also where compileScalaFiles() below points scalac's "-classpath" so that the Scala
   * sources can reference these already-compiled classes.
   */
  private static void compileJavaHelperFiles(DiagnosticCollector<JavaFileObject> diagnostic, File classOutputDir, File... files) throws PLMCompilerException
  {
    List<String> paths = Arrays.asList(files).stream().map(f -> f.toPath().toString()).toList();

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null)
      throw new PLMCompilerException("No system Java compiler available: PLM must run on a JDK, not a JRE.", new HashSet<>(paths), new Error(), diagnostic);

    try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostic, null, StandardCharsets.UTF_8)) {
      // Same reasoning as LangJava.compileJavaFiles(): CLASS_PATH is left empty so these files (and whatever student
      // Scala code ends up seeing them through scalac's own classpath) cannot see PLM's own classes.
      fileManager.setLocation(StandardLocation.CLASS_PATH, List.of());
      fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(classOutputDir));

      Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
      boolean success                                     = compiler.getTask(null, fileManager, diagnostic, null, null, compilationUnits).call();

      // Any diagnostic, even a warning, is treated as an error, same as everywhere else in this class.
      if (!success || !diagnostic.getDiagnostics().isEmpty()) {
        String rtStderr = diagnostic.getDiagnostics().stream().map(Object::toString).collect(Collectors.joining("\n"));
        throw new PLMCompilerException(rtStderr, new HashSet<>(paths), new Error(), diagnostic);
      }
    } catch (IOException e) {
      throw new PLMCompilerException(e.getMessage(), new HashSet<>(paths), new Error(), diagnostic);
    }
  }

  private static void compileScalaFiles(DiagnosticCollector<JavaFileObject> diagnostic, File packageFolder, File... files) throws PLMCompilerException
  {
    List<File> javaFiles  = Arrays.asList(files).stream().filter(f -> f.getName().endsWith(".java")).toList();
    List<File> scalaFiles = Arrays.asList(files).stream().filter(f -> f.getName().endsWith(".scala")).toList();
    List<String> paths    = Arrays.asList(files).stream().map(s -> s.toPath().toString()).toList();

    if (!javaFiles.isEmpty())
      compileJavaHelperFiles(diagnostic, packageFolder, javaFiles.toArray(File[] ::new));

    if (!scalaFiles.isEmpty()) {
      List<String> scalacArgs = new ArrayList<>();
      scalacArgs.add("-classpath");
      // packageFolder itself, where javac (above, if any) just wrote the already-compiled Java classes.
      scalacArgs.add(scalaCompilerClasspath() + File.pathSeparator + packageFolder.getAbsolutePath());
      scalacArgs.add("-d");
      scalacArgs.add(packageFolder.getAbsolutePath());
      scalacArgs.add("-color:never"); // avoid ANSI escapes polluting the captured diagnostic text below
      scalaFiles.forEach(f -> scalacArgs.add(f.getAbsolutePath()));
      runDotc(diagnostic, paths, scalacArgs);
    }
  }

  /**
   * dotc has no DiagnosticCollector-like API as stable as javac's (see the class-level comment): with no custom
   * Reporter passed in, Driver.process() uses its default ConsoleReporter, which prints diagnostics straight to
   * System.out/System.err. Those are JVM-wide, so DOTC_LOCK serializes compiles, and System.out/err are swapped for
   * capturing ByteArrayOutputStreams only for the duration of this call, then restored.
   */
  private static final Object DOTC_LOCK = new Object();

  /** Drives the Scala 3 compiler in-process (dotty.tools.dotc.Driver), mirroring LangJava's in-process javac call. */
  private static void runDotc(DiagnosticCollector<JavaFileObject> diagnostic, List<String> paths, List<String> args) throws PLMCompilerException
  {
    ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
    ByteArrayOutputStream errBuf = new ByteArrayOutputStream();
    Reporter reporter;

    synchronized (DOTC_LOCK) {
      PrintStream origOut = System.out;
      PrintStream origErr = System.err;
      System.setOut(new PrintStream(outBuf, true, StandardCharsets.UTF_8));
      System.setErr(new PrintStream(errBuf, true, StandardCharsets.UTF_8));
      try {
        reporter = new Driver().process(args.toArray(new String[0]));
      } finally {
        System.setOut(origOut);
        System.setErr(origErr);
      }
    }

    // Any diagnostic, even a mere warning, fails the compile.
    if (reporter.hasErrors() || reporter.hasWarnings()) {
      String rtStdout = outBuf.toString(StandardCharsets.UTF_8);
      String rtStderr = errBuf.toString(StandardCharsets.UTF_8);
      String msg      = "The following Scala 3 compilation failed: " + String.join(" ", args) + "\n" + (rtStderr.isBlank() ? rtStdout : rtStderr);
      throw new PLMCompilerException(msg, new HashSet<>(paths), new Error(), diagnostic);
    }
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

  @Override public String compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {
    String packageNameCache = packageNameForExercise(exo, whatToCompile);

    String mainRemoteContent = getRemoteScalaFile(null);

    String jarPath                                 = null;
    DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<JavaFileObject>();
    List<String> generatedSources = new ArrayList<>(); // kept to dump them on failure when debugging
    try {
      for (SourceFile sf : exo.getSourceFilesList(this)) {
        String key = packageNameCache + "." + sf.getName();

        String remote = sf.getRemote();

        // All the imports go on a single line, so that the line numbers of the generated code do not depend on how many there are
        String imports = ("import generated.ValueSerializer._; "
                          + "import java.awt.Color; "
                          + "import generated.Remote._; "
                          + "import generated." + remote + "._; " + sf.getImports())
                             .replace('\n', ' ');

        String template = "package generated\n\n" + imports + "\n\nobject Entity {\n" +
                          (whatToCompile == StudentOrCorrection.CORRECTION ? sf.getCorrectionTemplate() : sf.getTemplate()) + "\n}";
        String entityCode =
            sf.getCompilableContent(template, whatToCompile == StudentOrCorrection.CORRECTION ? sf.getCorrectionBody() : sf.getBody()).content();
        generatedSources.add(sf.getName() + ":" + entityCode);
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
          extraSourcePaths.addAll(REMOTE_EXTRA_SOURCE_FILES.getOrDefault(remote, List.of()));

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
          Files.writeString(valueSerializer.toPath(), rewriteExtraImports.apply(copyFileRenamingPackage("src/plm/core/ValueSerializer.java")));

          List<File> extraFiles = new ArrayList<>();
          for (String sourcePath : extraSourcePaths) {
            File extraFile = new File(workspace, new File(sourcePath).getName());
            Files.writeString(extraFile.toPath(), rewriteExtraImports.apply(copyFileRenamingPackage(sourcePath)));
            extraFiles.add(extraFile);
          }

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
          runJarTool(workspace, jarFile, "generated.Main", findClassFiles(workspace, diagnostic), diagnostic);

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
        for (String source : generatedSources)
          System.out.println("Source file " + source);

      throw e;
    }
    return jarPath;
  }

  /**
   * Runs "java -cp &lt;jarPath&gt;:&lt;scala runtime jars&gt; generated.Main &lt;socketPath&gt;", executable being the jar path
   * returned by compileExo() -- the main class is always "generated.Main". We cannot use "java -jar" alone because a jar's
   * Class-Path manifest attribute is only reliably resolved for relative paths, while the Scala jars' paths are probably
   * absolute, leading to silent failures at startup.
   */
  @Override protected ProcessBuilder buildProcess(String executable, Path socketPath) throws IOException
  {
    File exec = new File(executable);
    if (!exec.exists())
      throw new RuntimeException(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));

    return new ProcessBuilder("java", "-cp", executable + File.pathSeparator + scalaRuntimeClasspath(), "generated.Main", socketPath.toString());
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
