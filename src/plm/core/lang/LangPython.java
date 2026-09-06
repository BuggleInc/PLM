package plm.core.lang;

import java.io.*;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
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
import plm.universe.CommandExecutor;
import plm.universe.Direction;
import plm.universe.Entity;
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
public class LangPython extends ScriptingLanguage {
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
  File tempFolder                                    = new File(System.getProperty("java.io.tmpdir"), "plm_python_toremove");

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

  private static String extractRunFunction(String code) { return ExerciseTemplated.extractRunFunctionIndentBased(code, RUN_KEYWORD); }

  /**
   * Python counterpart of LangJava/LangScala's getCorrectedTemplate(), built on
   * ExerciseTemplated.extractRunSpanIndentBased() (Python has no braces -- see that method's javadoc). No class/object
   * wrapper is needed at all (unlike Java/Scala): Entity.py is just top-level function definitions, so the three cases
   * only decide how to concatenate $run/$body, not how to wrap them.
   */
  private static String getCorrectedTemplate(String correction)
  {
    int beginTemplateIndexRaw = correction.indexOf("# BEGIN TEMPLATE");
    int endTemplateIndex      = correction.indexOf("# END TEMPLATE");
    int endTemplateIndexEnd   = endTemplateIndex == -1 ? -1 : endTemplateIndex + "# END TEMPLATE".length();
    int runFunctionI          = correction.indexOf(RUN_KEYWORD);

    int[] runSpan = ExerciseTemplated.extractRunSpanIndentBased(correction, RUN_KEYWORD);

    if (runSpan == null) {
      // No "def run(" anywhere in the source at all: the templated text is meant to become run()'s entire body on its
      // own (e.g. a Buggle exercise whose whole solution is a handful of top-level statements/helper defs, with no
      // separate driving function) -- synthesize the wrapper ourselves rather than requiring an otherwise-empty
      // "def run():" to be hand-added to every such file. $bodyIndented (as opposed to $body elsewhere) tells
      // compileExo() this content needs a leading indent applied, since it comes straight from column 0 in the source.
      return "$imports\n\ndef run():\n$bodyIndented";
    }
    if (endTemplateIndex != -1 && beginTemplateIndexRaw <= runFunctionI && runFunctionI <= endTemplateIndex) {
      // run()'s own declaration falls inside the templated region: the templated text IS run() (signature included).
      return "$imports\n\n$body";
    }
    if (runSpan[0] <= beginTemplateIndexRaw && endTemplateIndexEnd <= runSpan[1]) {
      // The templated region sits fully inside an EXISTING run()'s indented body (already indented in the source, unlike
      // the runSpan==null case above), but run()'s own "def" line is outside it.
      return "$imports\n\ndef run():\n$body";
    }
    // run() and the templated region are disjoint (a separate templated function, run() elsewhere -- the common case
    // for the Bat/Cons exercises, whose run() was mechanically added precisely to make this uniform with Java/Scala).
    return "$imports\n\n$run\n\n$body";
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

  private static String getRemote(String code)
  {
    // Unlike Java/Scala's correction text (which carries package/class names identifying its universe), Python source
    // files have no such markers at all -- bare functions, no imports. The only thing to sniff is which primitive
    // functions the (mechanically-added, see the session history) run() actually calls. Markers must be unique to their
    // universe: e.g. getX()/getY() exist on BOTH Lander and Buggle, so they can't be used to tell them apart.
    if (code.contains("toRecListIfArray"))
      return "RemoteCons";
    if (code.contains("isFlying") || code.contains("simulateStep") || code.contains("getSpeedY") || code.contains("setDesiredThrust"))
      return "RemoteLander";
    if (code.contains("isFacingWall") || code.contains("isBackingWall") || code.contains("stepForward") || code.contains("stepBackward") ||
        code.contains("pickupBaggle") || code.contains("dropBaggle") || code.contains("isOverBaggle") || code.contains("isCarryingBaggle") ||
        code.contains("brushDown") || code.contains("brushUp") || code.contains("getGroundColor") || code.contains("getBrushColor") ||
        code.contains("hasTopWall") || code.contains("hasLeftWall") || code.contains("isWallOnLeft") || code.contains("isWallOnRight") ||
        code.contains("getIndicationBdr") || code.contains("haveSeenError") || code.contains("isOverMessage") || code.contains("clearMessage") ||
        code.contains("getWorldHeight") || code.contains("getWorldWidth") || code.contains("writeMessage") || code.contains("readMessage") ||
        code.contains("setPos") || code.contains("errorMsg") || code.contains("forward()") || code.contains("forward(") || code.contains("backward("))
      return "RemoteBuggle";
    if (code.contains("getTestCount") || code.contains("setTestResult"))
      return "RemoteBat";
    if (code.contains("Langton") || code.contains("Turmite"))
      return "RemoteTurmite";
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

    return null;
  }

  public String getRemotePythonFile(String remoteName)
  {
    String remote;
    if (remoteName == null || remoteName.isEmpty())
      remote = "Remote.py";
    else
      remote = remoteName;

    if (!remote.startsWith("Remote"))
      remote = "Remote" + remote;
    if (!remote.endsWith(".py"))
      remote = remote + ".py";

    String path = "resources/langages/python/" + remote;

    InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
    if (stream == null) {
      throw new IllegalArgumentException("Remote '" + path + "' do not exist (argument passed: '" + remoteName + "').");
    }

    return new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
  }

  private String packageName() { return "plm_python_run" + workspaceSuffix.get(); }

  @Override public void compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {
    workspaceSuffix.incrementAndGet();
    String runName = packageName();

    Map<String, String> runtimePatterns = new TreeMap<String, String>();

    try {
      for (SourceFile sf : exo.getSourceFilesList(this)) {
        String correction = sf.getCorrection();

        String remote = getRemote(correction);
        if (remote == null) {
          PLMCompilerException e = new PLMCompilerException("This universe is not implemented in Python.", null, null);
          exo.lastResult         = RunOutcome.newCompilationError(e.getMessage());
          throw e;
        }

        String runFunction = extractRunFunction(correction);
        String dependency  = extractRunDependency(correction);

        List<String> extraSourcePaths = remoteExtraSourceFiles.getOrDefault(remote, List.of());
        StringBuilder extraImports    = new StringBuilder();
        for (String sourcePath : extraSourcePaths)
          extraImports.append("from ").append(fileNameWithoutExtension(sourcePath)).append(" import *\n");

        runtimePatterns.put("\\$run", Matcher_quoteReplacement(runFunction));
        runtimePatterns.put("\\$dependency", Matcher_quoteReplacement(dependency));
        runtimePatterns.put("\\$imports",
                            ("from ValueSerializer import *\n" + "from Remote import *\n" + "from " + remote + " import *\n" + extraImports)
                                .replace('\n', '\u0001'));

        String template = getCorrectedTemplate(correction);

        String entityCode = template;
        for (Map.Entry<String, String> e : runtimePatterns.entrySet())
          entityCode = entityCode.replaceAll(e.getKey(), e.getValue());
        entityCode = entityCode.replace("$bodyIndented", Matcher_quoteReplacement(indent(stripMarkers(correction))));
        entityCode = entityCode.replace("$body", Matcher_quoteReplacement(stripMarkers(correction)));
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

        Files.writeString(new File(workspace, "Template.txt").toPath(), template);
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
            PLMCompilerException e = new PLMCompilerException(output, Set.of(entityFile.toString()), new Error(), null);
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

  @Override public ArrayList<Entity> mutateEntities(Exercise exo, List<Entity> olds, StudentOrCorrection whatToMutate) throws PLMCompilerException
  {
    List<SourceFile> sourceFile = exo.getSourceFilesList(this);

    if (sourceFile.size() != 1)
      throw new IllegalStateException("ToBeYetImplemented: Cannot differentiate entity scripts for now.");

    SourceFile source = sourceFile.get(0);

    for (Entity old : olds) {
      String path = source.meta.get("PYTHON");
      if (path == null)
        new Exception("stack trace for null-path mutateEntities call").printStackTrace();
      if (path != null) {
        old.setScript(this, path);
      }
    }

    return new ArrayList<>(olds);
  }

  @Override public void runEntity(final Entity ent, final RunOutcome progress)
  {
    final StringBuffer resEvaluationError = new StringBuffer();

    try {
      String executable = ent.getScript(this);
      if (executable == null)
        throw new IllegalStateException("TOFIX");

      File exec = new File(executable);
      if (!exec.exists())
        throw new RuntimeException(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));

      Path socketDir                    = Files.createTempDirectory("plm-python-sock-");
      Path socketPath                   = socketDir.resolve("protocol.sock");
      ServerSocketChannel serverChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
      serverChannel.bind(UnixDomainSocketAddress.of(socketPath));
      serverChannel.configureBlocking(false);
      Selector selector = Selector.open();
      serverChannel.register(selector, SelectionKey.OP_ACCEPT);

      ProcessBuilder pb = new ProcessBuilder("python3", exec.getName(), socketPath.toString());
      pb.directory(exec.getParentFile());
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
              CommandExecutor.command(ent, str, bwriter);
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

      System.err.println("XXX Generating " + folder + "/" + name + ".py");
      Files.writeString(new File(folder, name + ".py").toPath(), code);
    }
  }
}
