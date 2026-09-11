package plm.core.lang;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
 * Remote execution for Python, mirroring LangJava's architecture as closely as possible on purpose (see the class-level
 * comment there): an external "python3" process is spawned per run, talking back to CommandExecutor over a UNIX domain
 * socket -- same protocol as Java/Scala/C.
 *
 * Not yet factored with LangJava (structure kept close on purpose to make that factoring easy later). Substantially
 * simpler than LangJava/LangScala in one respect: Python needs no compilation step at all (just write the .py files and
 * spawn "python3 Main.py <socket>"), and no import-rewriting for its copied support files (ValueSerializer.py, RecList.py):
 * Python resolves "from X import *" by file presence in the working directory, not by a package-qualified name the way
 * Java/Scala do, so there is no analogue of the ClassCastException-class bug LangJava/LangScala had to work around there.
 */
public class LangPython extends TemplatedRemoteLang {
  /**
   * Extra source files to be copied alongside the student's code (unlike LangJava/LangScala, no per-universe
   * "coreExtraSourceFiles vs remoteExtraSourceFiles" split is needed: ValueSerializer.py has no external dependency of its
   * own to drag in, so it doesn't need to always be paired with anything the way ValueSerializer.java needs Point.java).
   */
  private static final Map<String, List<String>> remoteExtraSourceFiles =
      Map.of("RemoteCons", List.of("lib/resources/langages/python/RecList.py"));

  private static String brokenLanguageMessage;
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;

  private static final AtomicInteger workspaceSuffix = new AtomicInteger();
  File tempFolder                                    = TMP_ROOT.resolve("python").toFile();

  public LangPython() { super("Python", "py", ResourcesCache.getIcon("img/lang_python.png")); }
  @Override public boolean isPython() { return true; }

  @Override public String getBrokenLanguageMessage() { return brokenLanguageMessage; }
  @Override public boolean isBrokenLanguage()
  {
    if (brokenLanguageState == BrokenLanguageState.Unitialized) {
      try {
        Process proc    = new ProcessBuilder("python3", "--version").start();
        int retcode     = proc.waitFor();
        if (retcode != 0) {
          brokenLanguageMessage = Game.i18n.tr("python3 exited with an error while checking its version.");
          brokenLanguageState   = BrokenLanguageState.NotUsable;
          return false;
        }
        brokenLanguageState = BrokenLanguageState.Usable;
      } catch (IOException | InterruptedException e) {
        brokenLanguageMessage = Game.i18n.tr("Cannot run python3: {0}. Is Python 3 installed and on the PATH?", e.getMessage());
        System.err.println(brokenLanguageMessage);
        brokenLanguageState = BrokenLanguageState.NotUsable;
        return false;
      }
    }
    return brokenLanguageState != BrokenLanguageState.Usable;
  }

  private static String fileNameWithoutExtension(String path)
  {
    String name = new File(path).getName();
    int dot     = name.lastIndexOf('.');
    return dot < 0 ? name : name.substring(0, dot);
  }

  private static final String RUN_KEYWORD = "def run(";

  /**
   * Python counterpart of {@link TemplatedRemoteLang#extractRunSpan}: Python has no braces, so a function's body is
   * delimited by indentation instead -- it ends at the first subsequent non-blank line indented no more than the
   * "def" line itself (or at end of file). Tabs and spaces are counted as plain characters (not expanded), which only
   * matters if a single file mixes the two inconsistently -- not a case seen in any exercise file so far.
   */
  @Override protected int[] extractRunSpan(String code, String runKeyword)
  {
    int startRun = code.indexOf(runKeyword);
    if (startRun == -1)
      return null;

    int beginOfRunLine = code.substring(0, startRun).lastIndexOf('\n');
    beginOfRunLine     = beginOfRunLine == -1 ? 0 : beginOfRunLine + 1;

    int defIndent = startRun - beginOfRunLine;

    int lineEnd = code.indexOf('\n', startRun);
    if (lineEnd == -1)
      lineEnd = code.length();

    int pos = lineEnd + 1;
    int end = lineEnd;
    while (pos <= code.length()) {
      int nextLineEnd = code.indexOf('\n', pos);
      if (nextLineEnd == -1)
        nextLineEnd = code.length();

      String line    = code.substring(pos, nextLineEnd);
      String trimmed = line.strip();

      if (!trimmed.isEmpty()) {
        int indent = 0;
        while (indent < line.length() && (line.charAt(indent) == ' ' || line.charAt(indent) == '\t'))
          indent++;
        if (indent <= defIndent)
          break;
      }

      end = nextLineEnd;
      if (nextLineEnd == code.length())
        break;
      pos = nextLineEnd + 1;
    }

    return new int[] {beginOfRunLine, end};
  }

  private String extractRunFunction(String code) { return extractRunFunction(code, RUN_KEYWORD); }

  /**
   * Python counterpart of LangJava/LangScala's getCorrectedTemplate(), built on
   * ExerciseTemplated.extractRunSpanIndentBased() (Python has no braces -- see that method's javadoc). No class/object
   * wrapper is needed at all (unlike Java/Scala): Entity.py is just top-level function definitions, so the three cases
   * only decide how to concatenate $run/$body, not how to wrap them.
   */
  private record CorrectedTemplate(String template, String bodySource) {}

  /**
   * Python counterpart of LangJava/LangScala's getCorrectedTemplate(), built on
   * ExerciseTemplated.extractRunSpanIndentBased() (Python has no braces -- see that method's javadoc). No class/object
   * wrapper is needed at all (unlike Java/Scala): Entity.py is just top-level function definitions, so the four cases
   * only decide how to concatenate $run/$body, not how to wrap them.
   */
  private CorrectedTemplate getCorrectedTemplate(String correction)
  {
    int beginTemplateIndexRaw = correction.indexOf("# BEGIN TEMPLATE");
    int endTemplateIndex      = correction.indexOf("# END TEMPLATE");
    int endTemplateIndexEnd   = endTemplateIndex == -1 ? -1 : endTemplateIndex + "# END TEMPLATE".length();
    int runFunctionI          = correction.indexOf(RUN_KEYWORD);

    int[] runSpan = extractRunSpan(correction, RUN_KEYWORD);

    if (runSpan == null) {
      // No "def run(" anywhere in the source at all: the templated text is meant to become run()'s entire body on its
      // own (e.g. a Buggle exercise whose whole solution is a handful of top-level statements/helper defs, with no
      // separate driving function) -- synthesize the wrapper ourselves rather than requiring an otherwise-empty
      // "def run():" to be hand-added to every such file. $bodyIndented (as opposed to $body elsewhere) tells
      // compileExo() this content needs a leading indent applied, since it comes straight from column 0 in the source.
      return new CorrectedTemplate("$imports\n\ndef run():\n$bodyIndented", correction);
    }
    if (endTemplateIndex != -1 && beginTemplateIndexRaw <= runFunctionI && runFunctionI <= endTemplateIndex) {
      // run()'s own declaration falls inside the templated region: the templated text IS run() (signature included),
      // so $body -- built from the whole correction -- already is a single, complete, self-contained "def run(): ..."
      // and needs no wrapper of its own.
      return new CorrectedTemplate("$imports\n\n$body", correction);
    }
    if (runSpan[0] <= beginTemplateIndexRaw && endTemplateIndexEnd <= runSpan[1]) {
      // The templated region sits fully inside an EXISTING run()'s indented body (already indented in the source, unlike
      // the runSpan==null case above), but run()'s own "def" line is outside it. $body is still the whole correction,
      // which -- exactly like the case just above -- already starts with that "def run():" line: it's a complete
      // definition on its own and must NOT be wrapped in another "def run():", or the outer one becomes a dead
      // function that defines an inner "run" and never calls it.
      return new CorrectedTemplate("$imports\n\n$body", correction);
    }
    // run() and the templated region are disjoint (a separate templated function, run() elsewhere -- the common case
    // for the Bat/Cons exercises, whose run() was mechanically added precisely to make this uniform with Java/Scala).
    // $run already reproduces run() verbatim, so exclude its span from $body to avoid defining it a second time.
    String bodySource = correction.substring(0, runSpan[0]) + correction.substring(runSpan[1]);
    return new CorrectedTemplate("$imports\n\n$run\n\n$body", bodySource);
  }

  private static String extractRunDependency(String code)
  {
    StringBuilder section = new StringBuilder();
    for (int i = 0; i < code.length(); i++) {
      if (!code.startsWith("# BEGIN DEPENDENCY", i))
        continue;
      int begin = code.indexOf('\n', i) + 1;
      int end   = code.indexOf("# END DEPENDENCY", i);
      if (end == -1)
        break;
      section.append(code, begin, end).append("\n");
      i = end + "# END DEPENDENCY".length();
    }
    return section.toString();
  }

  protected static String getRemote(String code)
  {
    // Python exercises must declare their universe explicitly with a real "from RemoteXxx import *" line
    Matcher explicit = Pattern.compile("(?m)^from (Remote\\w+) import \\*").matcher(code);
    if (explicit.find())
      return explicit.group(1);

    // If there is no such explicit import, fail fast and get the exercise author fix the issue
    return null;
  }

  public String getRemotePythonFile(String remoteName) { return loadRemoteFile(remoteName, "python", ".py"); }

  protected String packageName() { return "plm_python_run" + workspaceSuffix.get(); }

  @Override public void compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {
    workspaceSuffix.incrementAndGet();
    String runName = packageName();

    Map<String, String> runtimePatterns = new TreeMap<String, String>();

    try {
      for (SourceFile sf : exo.getSourceFilesList(this)) {
        String correction = sf.getCorrection();

        String remote = getRemoteOrFail(correction, "Python", exo, null);

        String runFunction = extractRunFunction(correction);
        String dependency  = extractRunDependency(correction);

        List<String> extraSourcePaths = remoteExtraSourceFiles.getOrDefault(remote, List.of());
        StringBuilder extraImports    = new StringBuilder();
        for (String sourcePath : extraSourcePaths)
          extraImports.append("from ").append(fileNameWithoutExtension(sourcePath)).append(" import *\n");

        runtimePatterns.put("\\$run", Matcher_quoteReplacement(runFunction));
        runtimePatterns.put("\\$dependency", Matcher_quoteReplacement(dependency));
        runtimePatterns.put("\\$imports", ("from ValueSerializer import *\n"
                                           + "from Remote import *\n" + extraImports)
                                              .replace('\n', '\u0001'));

        CorrectedTemplate corrected = getCorrectedTemplate(correction);

        String entityCode = corrected.template();
        for (Map.Entry<String, String> e : runtimePatterns.entrySet())
          entityCode = entityCode.replaceAll(e.getKey(), e.getValue());
        entityCode = entityCode.replace("$bodyIndented", Matcher_quoteReplacement(indent(stripMarkers(corrected.bodySource()))));
        entityCode = entityCode.replace("$body", Matcher_quoteReplacement(stripMarkers(corrected.bodySource())));
        entityCode = entityCode.replace('\u0001', '\n');

        File workspace = new File(tempFolder, runName + "_" + sf.getName().replaceAll("[^a-zA-Z0-9]", "_"));
        // noinspection ResultOfMethodCallIgnored
        workspace.mkdirs();

        File valueSerializer = new File(workspace, "ValueSerializer.py");
        File mainRemote       = new File(workspace, "Remote.py");
        File entityRemote     = new File(workspace, remote + ".py");
        File entityFile       = new File(workspace, "Entity.py");
        File mainFile         = new File(workspace, "Main.py");

        String mainContent = "import sys\n" + "from Remote import connect\n" + "from Entity import run\n" + "\n" + "connect(sys.argv[1])\n" + "run()\n";

        List<File> extraFiles = new ArrayList<>();
        for (String sourcePath : extraSourcePaths) {
          File extraFile = new File(workspace, new File(sourcePath).getName());
          Files.copy(new File(sourcePath).toPath(), extraFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
          extraFiles.add(extraFile);
        }

        Files.writeString(new File(workspace, "Correction.txt").toPath(), correction);
        Files.copy(new File("lib/resources/langages/python/ValueSerializer.py").toPath(), valueSerializer.toPath(),
                   java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        Files.writeString(mainRemote.toPath(), getRemotePythonFile(null));
        Files.writeString(entityRemote.toPath(), getRemotePythonFile(remote));
        Files.writeString(entityFile.toPath(), entityCode);
        Files.writeString(mainFile.toPath(), mainContent);

        // No compilation step for Python: syntax errors only surface when the process actually runs. Do a quick
        // "python3 -m py_compile" pass here so compile-time errors are reported at compile time, matching the other
        // languages' UX, instead of silently at first run.
        try {
          Process proc = new ProcessBuilder("python3", "-m", "py_compile", "Entity.py").directory(workspace).redirectErrorStream(true).start();
          String output = new BufferedReader(new InputStreamReader(proc.getInputStream())).lines().collect(Collectors.joining("\n"));
          int retcode   = proc.waitFor();
          if (retcode != 0) {
            PLMCompilerException e = new PLMCompilerException("Compiling " + entityFile.toString() + " yielded the following output:\n" + output,
                                                              Set.of(entityFile.toString()), new Error(), null);
            exo.lastResult         = RunOutcome.newCompilationError(e.getMessage());
            if (out != null)
              out.log(e.getMessage());
            throw e;
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }

        sf.meta.put("PYTHON", mainFile.toPath().toString());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Escapes $ and \ for use as the replacement argument of String.replaceAll(). */
  private static String Matcher_quoteReplacement(String s) { return s.replace("\\", "\\\\").replace("$", "\\$"); }

  private static String stripMarkers(String code)
  {
    StringBuilder result = new StringBuilder();
    for (String line : code.split("\n", -1)) {
      String trimmed = line.strip();
      if (trimmed.startsWith("# BEGIN TEMPLATE") || trimmed.startsWith("# END TEMPLATE") || trimmed.startsWith("# BEGIN SOLUTION") ||
          trimmed.startsWith("# END SOLUTION") || trimmed.startsWith("# BEGIN DEPENDENCY") || trimmed.startsWith("# END DEPENDENCY"))
        continue;
      result.append(line).append("\n");
    }
    return result.toString();
  }

  /** Prefixes every non-empty line with 4 spaces -- used to nest column-0 source content under a synthesized "def run():"
   *  (see getCorrectedTemplate()'s $bodyIndented case). Blank lines are left untouched: Python doesn't require them
   *  indented, and leaving them alone avoids a trailing-whitespace-only line looking meaningfully different in diffs. */
  private static String indent(String code)
  {
    StringBuilder result = new StringBuilder();
    for (String line : code.split("\n", -1))
      result.append(line.isEmpty() ? line : "    " + line).append("\n");
    if (result.length() > 0)
      result.setLength(result.length() - 1); // drop the extra trailing "\n" split(-1)/append loop introduces
    return result.toString();
  }

  /** Runs "python3 &lt;executable&gt; &lt;socketPath&gt;" from the executable's own directory. */
  @Override protected ProcessBuilder buildProcess(String executable, Path socketPath) throws IOException
  {
    File exec = new File(executable);
    if (!exec.exists())
      throw new RuntimeException(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));

    ProcessBuilder pb = new ProcessBuilder("python3", exec.getName(), socketPath.toString());
    pb.directory(exec.getParentFile());
    return pb;
  }

  public static class LangPythonExternalPrimitiveGenerator implements ExternalPrimitiveLanguage {

    /** The Python argument names to use for this primitive's parameters -- shared between getPrototype() and
     *  getImplementation() so they can never drift apart (see getPrototype()'s comment on the forward()/backward()
     *  special case). Reflection-derived parameter names (PrimitiveParameter.name()) are usually synthetic ("arg0"
     *  etc.) unless the project happens to be compiled with -parameters, so this is the only place that matters. */
    List<String> getParameterNames(PrimitiveMethod method)
    {
      String name = method.name();
      if ((name.equals("forward") || name.equals("backward")) && method.parameters().size() == 1)
        return List.of("steps");
      return method.parameters().stream().map(PrimitiveParameter::name).toList();
    }

    String getPrototype(PrimitiveMethod method)
    {
      String name = method.name();
      List<String> paramNames = getParameterNames(method);
      String params = (name.equals("forward") || name.equals("backward")) ? "steps=1" : String.join(", ", paramNames);
      return "def " + name + "(" + params + ")";
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
      if (type == java.awt.Color.class)
        return "getAnswerColor()";
      if (type == Direction.class)
        return "getAnswerInt()";
      if (type == Point.class)
        return "getAnswerObject()";
      if (type == Point[].class)
        return "getAnswerObject()";
      if (type == Integer.class || type == int.class)
        return "getAnswerInt()";
      if (type == Boolean.class || type == boolean.class)
        return "getAnswerBoolean()";
      if (type == void.class || type == Void.class)
        return "";
      throw new IllegalStateException("Unknown type: " + type);
    }

    String getImplementation(PrimitiveMethod method)
    {
      String prototype = getPrototype(method);

      int id      = method.id();
      String name = method.name();

      String argsStr = getParameterNames(method).stream().map(s -> ", " + s).collect(Collectors.joining());
      String command = "    sendCommand(" + id + ", \"" + name + "\"" + argsStr + ")";

      String returning = method.hasReturn() ? "    return " + getReturning(method.output()) : "";

      return prototype + ":\n" + command + (returning.isEmpty() ? "" : "\n" + returning);
    }

    /**
     * Type declaration for a Java type involved in this universe's primitives, or "" if that type needs none.
     *
     * Only enums need this: Color doesn't (its Python mirror lives once and for all in ValueSerializer.py, imported
     * unconditionally by every generated file, unlike C which has no such shared runtime header to rely on), and every
     * other involved type (String, int, double, Point...) is either a builtin or already handled by ValueSerializer.
     *
     * Written generically over any enum (via reflection) rather than special-cased per class name the way LangC does:
     * plm.universe.Direction (Buggle: NORTH,EAST,SOUTH,WEST) and plm.universe.turtles.Direction (Turtle:
     * EAST,NORTH,WEST,SOUTH) order their constants differently, so a single hardcoded NORTH=0 would be wrong for one
     * of the two universes. Reflecting on whichever enum is actually involved gets the right constants for either.
     */
    String getTypeDeclaration(Class<?> type)
    {
      if (!type.isEnum())
        return "";

      StringBuilder sb = new StringBuilder("class " + type.getSimpleName() + ":\n");
      Object[] constants = type.getEnumConstants();
      for (int i = 0; i < constants.length; i++)
        sb.append("    ").append(constants[i]).append(" = ").append(i).append("\n");
      return sb.toString();
    }

    @Override public void generate(File folder, String name, List<PrimitiveMethod> methods) throws IOException { generate(folder, name, methods, ""); }

    @Override public void generate(File folder, String name, List<PrimitiveMethod> methods, String extraCode) throws IOException
    {
      final String typeDeclarations = ExternalPrimitiveLanguage.involved(methods)
                                           .stream()
                                           .sorted(Comparator.comparing(Class::getSimpleName))
                                           .map(this::getTypeDeclaration)
                                           .filter(s -> !s.isBlank())
                                           .collect(Collectors.joining("\n\n"));

      final String implementations = methods.stream().map(this::getImplementation).collect(Collectors.joining("\n\n\n"));

      String body = typeDeclarations.isBlank() ? implementations : typeDeclarations + "\n\n" + implementations;
      if (!extraCode.isBlank())
        body += "\n\n\n" + extraCode;

      final String code = "# THIS FILE IS GENERATED. DO NOT EDIT\nfrom Remote import *\n\n\n" + body + "\n";

      Files.writeString(new File(folder, name + ".py").toPath(), code);
    }
  }
}
