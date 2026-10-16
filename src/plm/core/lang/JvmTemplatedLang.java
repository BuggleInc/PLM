package plm.core.lang;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import plm.core.PLMCompilerException;
import plm.core.lang.primitives.Answer;
import plm.core.lang.primitives.CodeCreation;
import plm.core.lang.primitives.ExternalPrimitiveLanguage;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveParameter;
import plm.core.model.LogWriter;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.session.EntityFileSegments;
import plm.core.model.session.SourceFile;

/**
 * Ancestor of Java and Scala: both compile to a runnable jar and generate their ExternalPrimitiveLanguage glue the
 *  same way, only differing in the exact syntax produced.
 */
public abstract class JvmTemplatedLang extends TemplatedRemoteLang {

  /** Per-language scratch directory, nested under the shared TMP_ROOT, e.g. .../plm/java or .../plm/scala. */
  protected final File tempFolder = TMP_ROOT.resolve(getExt()).toFile();

  /** The location of a line of the entity in a stack trace, e.g. "Entity.java:42". */
  private static final Pattern ENTITY_LOCATION = Pattern.compile("(Entity\\.(?:java|scala):)(\\d+)");

  /** Name of the jar holding the glue classes of the language, deployed in TMP_ROOT */
  private final String entitiesJarName;

  public JvmTemplatedLang(String lang, String ext, ImageIcon i, String entitiesJarName)
  {
    super(lang, ext, i);
    this.entitiesJarName = entitiesJarName;
  }

  /** The beginning of the generated Entity file: the package, the imports (on a single line, then the ones of the entity file) and the opening of Entity */
  protected abstract String entityHeader(String remote, String userImports);

  /** Adapts the generated Entity to the language: it is a flat class, and its primitives are typed with the glue's types */
  protected abstract String adaptEntityCode(String entityCode);

  /** The content of the Main file connecting to the PLM and running the Entity */
  protected abstract String mainSource();

  /** Compiles the Entity and Main files, and packs the classes into jarFile, whose main class is generated.Main */
  protected abstract void compileToJar(DiagnosticCollector<JavaFileObject> diagnostic, File workspace, File entitiesJar, int lineShift, File mainFile,
                                       File entityFile, File jarFile) throws PLMCompilerException;

  /** Generates, compiles and packs the Entity of each source file of the exercise. Returns the path of the last jar. */
  @Override public String compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {
    String packageNameCache = packageNameForExercise(exo, whatToCompile);
    Path entitiesJar        = deployResource(entitiesJarName, TMP_ROOT);

    String jarPath                                 = null;
    DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<JavaFileObject>();
    for (SourceFile sf : exo.getSourceFilesList(this)) {
      EntityFileSegments segments = exo.getSegments(this, sf, whatToCompile);
      String pre                  = entityHeader(sf.getRemote(), sf.getImports()) + segments.pre();
      int offset                  = countLinesBeforeBody(pre);
      String entityCode           = pre + segments.body() + " \n" + segments.post() + "\n}";
      entityCode                  = entityCode.replace('\u00A0', ' '); // Kill those damn \160 chars (non-breaking spaces from copy/pasted examples?)
      entityCode                  = Pattern.compile("this\\.").matcher(entityCode).replaceAll("");
      entityCode                  = adaptEntityCode(entityCode);

      File workspace = new File(tempFolder, packageNameCache.replace('.', '/'));
      // noinspection ResultOfMethodCallIgnored
      workspace.mkdirs();

      File entityFile = new File(workspace, "Entity." + getExt());
      File mainFile   = new File(workspace, "Main." + getExt());
      File jarFile    = new File(workspace, "Code.jar");

      try {
        // The entities jar holds the helpers (Point, ValueSerializer...) in the "generated" package: make the imports refer to them
        for (String helper : CodeCreation.JAVA_HELPER_SOURCES)
          entityCode = entityCode.replace("import " + fqcnFromSourcePath(helper) + ";", "import generated." + fileNameWithoutExtension(helper) + ";");

        Files.writeString(entityFile.toPath(), entityCode);
        Files.writeString(mainFile.toPath(), mainSource());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      // The correction is not shifted: its body is the raw entity span, which does not start at the first line of the editor
      int lineShift = whatToCompile == StudentOrCorrection.STUDENT ? offset : 0;
      compileToJar(diagnostic, workspace, entitiesJar.toFile(), lineShift, mainFile, entityFile, jarFile);

      jarPath = jarFile.toPath().toString();
      lineShifts.put(jarPath, lineShift);
    }
    return jarPath;
  }

  @Override protected String shiftLocations(String line, String executable) { return shiftLines(ENTITY_LOCATION, line, executable); }

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
   * and extension) differs, and is left to subclasses. getReturning() is syntactically identical in both (same getAnswerXxx()
   * wire-side method names), except for the casts of the Points, which LangScala overrides.
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
      return switch (Answer.of(type)) {
        case STRING -> "getAnswerString()";
        case DOUBLE -> "getAnswerDouble()";
        case CHAR -> "getAnswerChar()";
        case COLOR -> "getAnswerColor()";
        case INT -> "getAnswerInt()";
        case BOOLEAN -> "getAnswerBoolean()";
        case POINT -> "(Point)getAnswerObject()";
        case POINT_ARRAY -> "(Point[])getAnswerObject()";
      };
    }

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
