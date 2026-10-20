package plm.core.lang;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
import plm.core.ui.ResourcesCache;
import plm.universe.Direction;
import plm.universe.Point;

public class LangJava extends JvmTemplatedLang {
  /* Language detection logic */
  private static String brokenLanguageMessage;
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;

  /** The precompiled glue (Remote*, ValueSerializer, Point...), shipped as a resource and deployed in TMP_ROOT. */
  private static final String ENTITIES_JAR = "plm-entities-java.jar";

  public LangJava() { super("Java", "java", ResourcesCache.getIcon("img/lang_java.png"), ENTITIES_JAR); }

  @Override public ExternalPrimitiveLanguage getPrimitiveGenerator() { return new LangJavaExternalPrimitiveGenerator(); }

  /**
   * Compiles the files in-process. Unless debugging is enabled, the line numbers reported for Entity.java are decreased by
   * {@code lineShift}, the number of lines of the generated source before the body, to match the lines of the editor.
   */
  private static void compileJavaFiles(DiagnosticCollector<JavaFileObject> diagnostic, File classOutputDir, int lineShift, File entitiesJar, File... files)
      throws PLMCompilerException
  {
    for (File javaFile : files) {

      String path = javaFile.toPath().toString();
      if (!path.endsWith(".java")) {
        throw new PLMCompilerException("Trying to compile a non java file: '" + path + "'", Set.of(path), new Error());
      }
    }

    List<String> paths = Arrays.asList(files).stream().map(s -> s.toPath().toString()).toList();

    // Do not start an external javac process that takes time to kick in, but use javax.tools.JavaCompiler (the API "javac" itself is built on)
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null)
      throw new PLMCompilerException("No system Java compiler available: PLM must run on a JDK, not a JRE.", new HashSet<>(paths), new Error());

    try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostic, null, StandardCharsets.UTF_8)) {
      // CLASS_PATH is the entities jar only, instead of java.class.path, so that the student code cannot see PLM's own classes.
      // CLASS_OUTPUT is classOutputDir itself, so ".class" files land nested under their "generated/" package folder.
      fileManager.setLocation(StandardLocation.CLASS_PATH, List.of(entitiesJar));
      fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(classOutputDir));

      Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
      boolean success                                     = compiler.getTask(null, fileManager, diagnostic, null, null, compilationUnits).call();

      // Any diagnostic, even a warning, is treated as an error.
      if (!success || !diagnostic.getDiagnostics().isEmpty()) {
        String rtStderr = diagnostic.getDiagnostics()
                              .stream()
                              .map(d -> {
                                boolean inBody = !Game.getInstance().isDebugEnabled() && d.getSource() != null &&
                                                 d.getSource().isNameCompatible("Entity", JavaFileObject.Kind.SOURCE) && d.getLineNumber() > lineShift;
                                return inBody ? "Entity.java:" + (d.getLineNumber() - lineShift) + ": " + d.getMessage(null) : d.toString();
                              })
                              .collect(Collectors.joining("\n"));
        throw new PLMCompilerException(rtStderr, new HashSet<>(paths), new Error());
      }
    } catch (IOException e) {
      throw new PLMCompilerException(e.getMessage(), new HashSet<>(paths), new Error());
    }
  }

  private static void createJarFile(DiagnosticCollector<JavaFileObject> diagnostic, File root, File packageFolder, File jarFile, File mainFile)
      throws PLMCompilerException
  {

    if (!packageFolder.toPath().toString().startsWith(root.toPath().toString())) {
      throw new PLMCompilerException("Root folder (" + root.toPath() + ") is not above package folder (" + packageFolder.toPath() + ") in file hierarchy.",
                                     Set.of(), new Error());
    }

    // Every class compiles under the fixed "generated" package (see compileJavaFiles(), which sets CLASS_OUTPUT to
    // packageFolder itself), so ".class" files land nested under a "generated/" folder there, same as any normal -d
    // compile -- walk packageFolder for them (findClassFiles()) rather than deriving their location from the ".java"
    // source files, which no longer tells us where javac put the output.
    String mainFileDotPath = "generated." + mainFile.getName().substring(0, mainFile.getName().indexOf('.'));

    runJarTool(packageFolder, jarFile, mainFileDotPath, findClassFiles(packageFolder, diagnostic), diagnostic);
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

  @Override protected String entityHeader(String remote, String userImports)
  {
    String imports = ("import static generated.ValueSerializer.*;\n"
                      + "import java.awt.Color;\n"
                      + "import static generated.Remote.*;\n"
                      + "import static generated." + remote + ".*;\n" + userImports)
                         .replace('\n', ' ');
    return "package generated;\n\n" + imports + "\n\npublic class Entity {\n";
  }

  @Override protected String adaptEntityCode(String entityCode)
  {
    entityCode = Pattern.compile("([^a-zA-Z])(Direction)([^a-zA-Z.])").matcher(entityCode).replaceAll("$1int$3");
    return Pattern.compile("@Override").matcher(entityCode).replaceAll("");
  }

  @Override protected String mainSource()
  {
    return "package generated;\n"
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
  }

  @Override
  protected void compileToJar(DiagnosticCollector<JavaFileObject> diagnostic, File workspace, File entitiesJar, int lineShift, File mainFile, File entityFile,
                              File jarFile) throws PLMCompilerException
  {
    compileJavaFiles(diagnostic, workspace, lineShift, entitiesJar, mainFile, entityFile);
    createJarFile(diagnostic, tempFolder, workspace, jarFile, mainFile);
  }

  /** Runs "java -cp &lt;executable&gt;:&lt;entities jar&gt; generated.Main &lt;port&gt;", the executable being the jar path produced by compileExo(). */
  @Override protected ProcessBuilder buildProcess(String executable, int port) throws IOException
  {
    File exec = new File(executable);
    if (!exec.exists())
      throw new RuntimeException(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));

    return new ProcessBuilder("java", "-cp", executable + File.pathSeparator + TMP_ROOT.resolve(ENTITIES_JAR), "generated.Main", String.valueOf(port));
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
      if (type == byte[].class)
        return "byte[]";
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

    @Override public String signature(PrimitiveMethod method, String name)
    {
      return getLanguageType(method.output()) + " " + name + "(" + method.parameters().stream().map(this::getParameter).collect(Collectors.joining(", ")) + ")";
    }

    String getPrototype(PrimitiveMethod method) { return "public static " + signature(method, method.name()); }

    String getImplementation(PrimitiveMethod method)
    {
      String prototype = getPrototype(method);

      String name = method.command(); // the PLM only knows the canonical name of the translated aliases

      String command =
          "\tsendCommand(\"" + name + "\"" + method.parameters().stream().map(PrimitiveParameter::name).map(s -> ", " + s).collect(Collectors.joining()) + ");";

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