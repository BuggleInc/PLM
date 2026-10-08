package plm.core.lang;

import dotty.tools.dotc.Driver;
import dotty.tools.dotc.interfaces.Diagnostic;
import dotty.tools.dotc.interfaces.ReporterResult;
import dotty.tools.dotc.interfaces.SourcePosition;
import java.awt.Color;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import plm.core.PLMCompilerException;
import plm.core.lang.primitives.CodeCreation;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveParameter;
import plm.core.model.Game;
import plm.core.model.LogWriter;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.session.EntityFileSegments;
import plm.core.model.session.SourceFile;
import plm.core.ui.ResourcesCache;
import plm.universe.Direction;
import plm.universe.Point;

/**
 * Remote execution for Scala, mirroring LangJava's architecture as closely as possible on purpose (see the class-level comment
 * there): an external "java" process is spawned per run, running compiled Scala bytecode that talks back to CommandExecutor
 * over a UNIX domain socket. Compilation happens entirely in-process: the Scala 3 compiler shipped as a PLM dependency is driven
 * directly through dotty.tools.dotc.Driver. The ancillary files (Remote*, ValueSerializer, Point, RecList) are precompiled in
 * plm-entities-scala.jar, which is on the classpath of both the compilation and the run.
 *
 * The structure is kept close to LangJava's on purpose, even if we favor code readability and flow linearity over absolute code
 * factorization.
 */
public class LangScala extends JvmTemplatedLang {
  /* Language detection logic */
  private static String brokenLanguageMessage;
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;

  /** The precompiled glue (Remote*, ValueSerializer, Point...), shipped as a resource and deployed in TMP_ROOT. */
  private static final String ENTITIES_JAR = "plm-entities-scala.jar";

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

  public static String scalaCompilerClasspath()
  {
    String optional = classpathOf(false, SCALA_COMPILER_OPTIONAL_CLASSES);
    return classpathOf(true, SCALA_COMPILER_CLASSES) + File.pathSeparator + scalaRuntimeClasspath() + (optional.isEmpty() ? "" : File.pathSeparator + optional);
  }

  private static String scalaRuntimeClasspath() { return classpathOf(true, SCALA_RUNTIME_CLASSES); }

  /**
   * Compiles the Scala files into packageFolder, against the Scala runtime and the precompiled entities jar. Unless debugging is
   * enabled, the line numbers reported for Entity.scala are decreased by {@code lineShift}, the number of lines of the generated
   * source before the body, to match the lines of the editor.
   */
  private static void compileScalaFiles(File packageFolder, File entitiesJar, int lineShift, File... files) throws PLMCompilerException
  {
    List<String> paths = Arrays.stream(files).map(f -> f.toPath().toString()).toList();
    List<String> scalacArgs = new ArrayList<>(List.of("-classpath", scalaCompilerClasspath() + File.pathSeparator + entitiesJar.getAbsolutePath(), "-d",
                                                      packageFolder.getAbsolutePath(), "-color:never", "-no-indent",
                                                      "-deprecation")); // no ANSI escapes; braces only (sources mix tabs and spaces); detailed warnings
    for (File file : files)
      scalacArgs.add(file.getAbsolutePath());
    runDotc(paths, scalacArgs, lineShift);
  }

  /**
   * Drives the Scala 3 compiler in-process (dotty.tools.dotc.Driver), mirroring LangJava's in-process javac call. The diagnostics
   * are collected through dotc's SimpleReporter interface. Any of them, even a mere warning, fails the compile.
   */
  private static void runDotc(List<String> paths, List<String> args, int lineShift) throws PLMCompilerException
  {
    List<String> messages = new ArrayList<>();
    ReporterResult result = new Driver().process(args.toArray(new String[0]), diagnostic -> messages.add(render(diagnostic, lineShift)), null);

    if (result.hasErrors() || result.hasWarnings())
      throw new PLMCompilerException(String.join("\n", messages), new HashSet<>(paths), new Error());
  }

  /** "file:line:col: message", then the source line and a caret under the column. Positions are 0-based in dotc, 1-based here. */
  private static String render(Diagnostic diagnostic, int lineShift)
  {
    if (diagnostic.position().isEmpty())
      return diagnostic.message();

    SourcePosition position = diagnostic.position().get();
    String file             = position.source().name();
    int line                = position.line() + 1;
    if (file.equals("Entity.scala") && !Game.getInstance().isDebugEnabled() && line > lineShift)
      line -= lineShift;

    return file + ":" + line + ":" + (position.column() + 1) + ": " + diagnostic.message() + "\n" + position.lineContent().stripTrailing() + "\n"
        + " ".repeat(position.column()) + "^";
  }

  @Override public String compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {
    String packageNameCache = packageNameForExercise(exo, whatToCompile);

    Path entitiesJar = deployResource(ENTITIES_JAR, TMP_ROOT);

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

        EntityFileSegments segments = sf.getSegments(whatToCompile);
        String pre                  = "package generated\n\n" + imports + "\n\nobject Entity {\n" + segments.pre();
        int offset                  = countLinesBeforeBody(pre);
        String entityCode           = pre + segments.body() + " \n" + segments.post() + "\n}";
        entityCode                  = entityCode.replace('\u00A0', ' '); // Kill those damn \160 chars (non-breaking spaces from copy/pasted examples?)
        generatedSources.add(sf.getName() + ":" + entityCode);
        entityCode        = Pattern.compile("([^a-zA-Z])(Direction)([^a-zA-Z.])").matcher(entityCode).replaceAll("$1Int$3");
        entityCode        = Pattern.compile("this\\.").matcher(entityCode).replaceAll("");
        // Scala's "override" needs a real supertype member to override, but Entity is a flat `object` extending nothing
        // so we strip "override"s just as LangJava strips "@Override" there for the exact same reason.
        // The trailing blanks go too: Scala 3 compares the indentation of members, which a leftover space would shift.
        entityCode = Pattern.compile("\\boverride[ \t]+").matcher(entityCode).replaceAll("");

        File workspace = new File(tempFolder, key.substring(0, key.lastIndexOf('.')).replace('.', '/'));
        workspace.mkdirs();

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
          // The entities jar holds the helpers (Point, ValueSerializer...) in the "generated" package: make the imports refer to them
          for (String helper : CodeCreation.JAVA_HELPER_SOURCES)
            entityCode = entityCode.replace("import " + fqcnFromSourcePath(helper) + ";", "import generated." + fileNameWithoutExtension(helper) + ";");

          Files.writeString(entityFile.toPath(), entityCode);
          Files.writeString(mainFile.toPath(), mainContent);

          // The correction is not shifted: its body is the raw entity span, which does not start at the first line of the editor
          int lineShift = whatToCompile == StudentOrCorrection.STUDENT ? offset : 0;
          compileScalaFiles(workspace, entitiesJar.toFile(), lineShift, mainFile, entityFile);

          // Scala compiles "object Main" to Main.class (plus a Main$.class holding the singleton); the manifest only
          // needs the former as its Main-Class entry point, exactly like a Java class with a static main().
          File jarFile = new File(workspace, "Code.jar");

          // Relative to workspace itself (where scalac actually wrote these under their package-name subdirectories),
          // not to its parent: this is what must end up as each entry's name inside the jar.
          // For example "plm/runtime4/Main.class" needs "-cp jarfile plm.runtime4.Main" to resolve.
          runJarTool(workspace, jarFile, "generated.Main", findClassFiles(workspace, diagnostic), diagnostic);

          jarPath = jarFile.toPath().toString();
          lineShifts.put(jarPath, lineShift);

        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (PLMCompilerException e) {
      System.err.println(Game.i18n.tr("Compilation error:"));
      exo.lastResult = RunOutcome.newCompilationError(this, e.getMessage());
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
   * Runs "java -cp &lt;jarPath&gt;:&lt;entities jar&gt;:&lt;scala runtime jars&gt; generated.Main &lt;socketPath&gt;", executable being the jar path
   * returned by compileExo() -- the main class is always "generated.Main". We cannot use "java -jar" alone because a jar's
   * Class-Path manifest attribute is only reliably resolved for relative paths, while the Scala jars' paths are probably
   * absolute, leading to silent failures at startup.
   */
  @Override protected ProcessBuilder buildProcess(String executable, Path socketPath) throws IOException
  {
    File exec = new File(executable);
    if (!exec.exists())
      throw new RuntimeException(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));

    return new ProcessBuilder("java", "-cp", executable + File.pathSeparator + TMP_ROOT.resolve(ENTITIES_JAR) + File.pathSeparator + scalaRuntimeClasspath(),
                              "generated.Main", socketPath.toString());
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

    @Override String getReturning(Class<?> type)
    {
      if (type == Point.class)
        return "getAnswerObject().asInstanceOf[Point]";
      if (type == Point[].class)
        return "getAnswerObject().asInstanceOf[Array[Point]]";
      return super.getReturning(type);
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

      final String outputString = getLanguageType(method.output());

      return "def " + name + "(" + parameters.stream().map(this::getParameter).collect(Collectors.joining(", ")) + "): " + outputString;
    }

    String getImplementation(PrimitiveMethod method)
    {
      String prototype = getPrototype(method);

      String name = method.name();

      String command = "\tsendCommand(\"" + name + "\"" +
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
