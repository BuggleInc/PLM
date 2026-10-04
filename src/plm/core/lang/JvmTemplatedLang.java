package plm.core.lang;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import plm.core.PLMCompilerException;
import plm.core.lang.primitives.ExternalPrimitiveLanguage;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveParameter;
import plm.core.model.Game;
import plm.universe.Direction;
import plm.universe.Point;

/**
 * Ancestor of Java and Scala: both compile to a runnable jar and generate their ExternalPrimitiveLanguage glue the
 *  same way, only differing in the exact syntax produced.
 */
public abstract class JvmTemplatedLang extends TemplatedRemoteLang {

  /** Extra source files to be copied alongside the student's code, keyed by the RemoteXxx universe needing them. */
  protected static final Map<String, List<String>> REMOTE_EXTRA_SOURCE_FILES =
      Map.of("RemoteCons", List.of("src/lessons/recursion/cons/universe/RecList.java"));

  /** Per-language scratch directory, nested under the shared TMP_ROOT, e.g. .../plm/java or .../plm/scala. */
  protected final File tempFolder = TMP_ROOT.resolve(getExt()).toFile();

  /** The location of a line of the entity in a stack trace, e.g. "Entity.java:42". */
  private static final Pattern ENTITY_LOCATION = Pattern.compile("(Entity\\.(?:java|scala):)(\\d+)");

  /** For each compiled jar, the number of lines to subtract from the locations of the entity in the stack traces: 0 for the correction. */
  protected final Map<String, Integer> lineShifts = new ConcurrentHashMap<>();

  public JvmTemplatedLang(String lang, String ext, ImageIcon i) { super(lang, ext, i); }

  /** Locations before the body, and all of them when debugging is enabled, are left untouched. */
  @Override protected String shiftLocations(String line, String executable)
  {
    int shift = lineShifts.getOrDefault(executable, 0);
    if (shift == 0 || Game.getInstance().isDebugEnabled())
      return line;
    return ENTITY_LOCATION.matcher(line).replaceAll(m -> {
      int lineNumber = Integer.parseInt(m.group(2));
      return m.group(1) + (lineNumber > shift ? lineNumber - shift : lineNumber);
    });
  }

  /**
   * e.g. "src/lessons/recursion/cons/universe/RecList.java" -> "lessons.recursion.cons.universe.RecList"
   */
  protected static String fqcnFromSourcePath(String sourcePath)
  {
    String withoutSrcPrefix = sourcePath.startsWith("src/") ? sourcePath.substring("src/".length()) : sourcePath;
    String withoutExtension = withoutSrcPrefix.replaceFirst("\\.(java|scala)$", "");
    return withoutExtension.replace('/', '.');
  }

  /**
   * e.g. "src/lessons/recursion/cons/universe/RecList.java" -> "RecList"
   */
  protected static String fileNameWithoutExtension(String path)
  {
    String name = new File(path).getName();
    int dot     = name.lastIndexOf('.');
    return dot < 0 ? name : name.substring(0, dot);
  }

  /**
   * Reads a source file to be copied verbatim alongside generated code (ValueSerializer.java, Point.java,
   * RecList.java...), rewriting its package declaration to "generated". Matches both Java's "package x.y.z;" and
   * Scala's "package x.y.z" (no trailing semicolon), and only adds the semicolon back for ".java" files.
   */
  protected static String copyFileRenamingPackage(String path) throws IOException
  {
    String content = Files.readString(new File(path).toPath(), StandardCharsets.UTF_8);
    return content.replaceFirst("package [^;\\n]*;?", "package generated" + (path.endsWith(".java") ? ";" : ""));
  }

  /**
   * How many lines of the generated source come before the body's own first line, so that the line number of a compiler
   * error or of a stack trace can be translated back into the student's own editor coordinates.
   */
  protected static int countLinesBeforeBody(String pre) { return (int)pre.chars().filter(c -> c == '\n').count(); }

  /**
   * ".class" files found under dir, as paths relative to dir itself -- e.g. "generated/Entity.class". Used to list
   * the entries a compiled exercise's jar needs, whichever language produced them.
   */
  protected static Set<String> findClassFiles(File dir, DiagnosticCollector<JavaFileObject> diagnostic) throws PLMCompilerException
  {
    try (var walk = Files.walk(dir.toPath())) {
      return walk.filter(p -> p.toString().endsWith(".class")).map(p -> dir.toPath().relativize(p).toString()).collect(Collectors.toSet());
    } catch (IOException e) {
      throw new PLMCompilerException(e.getMessage(), Set.of(), new Error());
    }
  }

  /**
   * Run "jar cfm &lt;jarFile&gt; &lt;manifest declaring Main-Class: mainClassDotPath&gt; &lt;classFiles...&gt;" from
   *  workDir, throwing if the tool reports anything on stderr.
   */
  protected static void runJarTool(File workDir, File jarFile, String mainClassDotPath, Set<String> classFiles, DiagnosticCollector<JavaFileObject> diagnostic)
      throws PLMCompilerException
  {
    File manifestFile = new File(jarFile.getParentFile(), "MANIFEST.MF");
    try {
      Files.writeString(manifestFile.toPath(), "Main-Class: " + mainClassDotPath + "\n");

      ArrayList<String> args = new ArrayList<>();
      args.add("jar");
      args.add("cfm");
      args.add(jarFile.toPath().toString());
      args.add(manifestFile.toPath().toString());
      args.addAll(classFiles);

      Process proc = Runtime.getRuntime().exec(args.toArray(String[] ::new), new String[] {}, workDir);

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

      String rtStdout = stdInput.lines().collect(Collectors.joining("\n"));
      String rtStderr = stdError.lines().collect(Collectors.joining("\n"));

      if (!rtStderr.isEmpty())
        throw new PLMCompilerException(rtStderr, new HashSet<>(classFiles), new Error());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Shared skeleton of Java/Scala's ExternalPrimitiveLanguage generator: computing the type declarations and method
   * implementations, and assembling them into a file, is identical between the two -- only the exact syntax produced
   * (getLanguageType, getParameter, getPrototype, getImplementation, the file's header/wrapper
   * and extension) differs, and is left to subclasses. getReturning() happens to be syntactically identical in both
   * (same getAnswerXxx() wire-side method names), so it is implemented once here.
   */
  public abstract static class JvmExternalPrimitiveGenerator implements ExternalPrimitiveLanguage {

    abstract String getLanguageType(Class<?> type);
    abstract String getTypeDeclaration(Class<?> type);
    abstract String getParameter(PrimitiveParameter parameter);
    abstract String getPrototype(PrimitiveMethod method);
    abstract String getImplementation(PrimitiveMethod method);

    /** File extension including the dot, e.g. ".java" or ".scala". */
    abstract String fileExtension();
    /**
     * Wrap the generated body (type declarations + method implementations + extra code) into the full file content:
     *  header comment, imports, and the language's class/object wrapper syntax.
     */
    abstract String wrapCode(String name, String body);

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
      if (type == Point.class)
        return "(Point)getAnswerObject()";
      if (type == Point[].class)
        return "(Point[])getAnswerObject()";
      if (type == Integer.class || type == int.class)
        return "getAnswerInt()";
      if (type == Boolean.class || type == boolean.class)
        return "getAnswerBoolean()";
      if (type == void.class || type == Void.class)
        return "";

      throw new IllegalStateException("Unknown type: " + type);
    }

    @Override public void generate(File folder, String name, List<PrimitiveMethod> methods) throws IOException { generate(folder, name, methods, ""); }

    @Override public void generate(File folder, String name, List<PrimitiveMethod> methods, String extraCode) throws IOException
    {
      Set<Class<?>> involved = ExternalPrimitiveLanguage.involved(methods);

      final String type_declarations = involved.stream().map(this::getTypeDeclaration).filter(o -> !o.isBlank()).collect(Collectors.joining("\n\n"));
      final String implementations   = methods.stream().map(this::getImplementation).collect(Collectors.joining("\n\n"));

      String body = "\n" + type_declarations + "\n" + implementations;
      if (!extraCode.isBlank())
        body += "\n" + extraCode;

      Files.writeString(new File(folder, name + fileExtension()).toPath(), wrapCode(name, body));
    }
  }
}
