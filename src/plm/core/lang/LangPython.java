package plm.core.lang;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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
import plm.core.model.session.EntityFileSegments;
import plm.core.model.session.SourceFile;
import plm.core.ui.ResourcesCache;
import plm.core.utils.Indentation;
import plm.universe.Direction;
import plm.universe.Point;

/**
 * Remote execution for Python, mirroring LangJava's architecture as closely as possible on purpose (see the class-level
 * comment there): an external "python3" process is spawned per run, talking back to CommandExecutor over a UNIX domain
 * socket -- same protocol as Java/Scala/C.
 *
 * The per-compile workspace name (packageNameForExercise(), shared with Java/Scala/C in TemplatedRemoteLang) is
 * factored; the rest of the pipeline isn't yet. Substantially simpler than LangJava/LangScala in one respect: Python
 * needs no compilation step at all (just write Entity.py and Main.py and spawn "python3 Main.py <socket>"), and no
 * import-rewriting for its support modules (ValueSerializer.py, Remote*.py, RecList.py): they are deployed once in ENTITIES_DIR,
 * which is on the PYTHONPATH of the run. Python resolves "from X import *" by module name, not by a package-qualified name the
 * way Java/Scala do, so there is no analogue of the ClassCastException-class bug LangJava/LangScala had to work around there.
 */
public class LangPython extends TemplatedRemoteLang {
  /**
   * Extra modules needed by some universes, besides ValueSerializer.py, Remote.py and the RemoteXxx.py of the universe (unlike
   * LangJava/LangScala, ValueSerializer.py has no external dependency of its own to drag in).
   */
  private static final Map<String, List<String>> remoteExtraModules = Map.of("RemoteCons", List.of("RecList.py"));

  /** The location of a line of the entity in a traceback or a syntax error, e.g. 'Entity.py", line 42'. */
  private static final Pattern ENTITY_LOCATION = Pattern.compile("(Entity\\.py\", line )(\\d+)");

  /** Where the modules above are deployed once and for all. Python caches their bytecode in its __pycache__ on first import. */
  private static final Path ENTITIES_DIR = TMP_ROOT.resolve("python-entities");

  private static String brokenLanguageMessage;
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;

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

  @Override public String compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {
    String runName = packageNameForExercise(exo, whatToCompile);

    String mainPath = null;

    try {
      for (SourceFile sf : exo.getSourceFilesList(this)) {
        String remote = sf.getRemote();

        List<String> extraModules  = remoteExtraModules.getOrDefault(remote, List.of());
        StringBuilder extraImports = new StringBuilder();
        for (String module : extraModules)
          extraImports.append("from ").append(fileNameWithoutExtension(module)).append(" import *\n");

        for (String module : List.of("ValueSerializer.py", "Remote.py", remote + ".py"))
          deployResource("python/" + module, ENTITIES_DIR);
        for (String module : extraModules)
          deployResource("python/" + module, ENTITIES_DIR);

        String imports              = "from ValueSerializer import *\nfrom Remote import *\n" + extraImports + sf.getImports();
        EntityFileSegments segments = sf.getSegments(whatToCompile);
        String body                 = segments.body();
        if (whatToCompile == StudentOrCorrection.STUDENT) {
          // The editor content is flush left, but the body sits bodyIndent spaces deep in the template: python needs it to be indented
          // accordingly. Code that is already indented (saved by a previous version) keeps its relative indentation.
          String code = Indentation.expandLeadingTabs(body);
          body        = Indentation.reindent(code, Indentation.minLeadingSpaces(code), sf.getBodyIndent());
        }
        String pre        = imports + "\n\n" + segments.pre();
        int offset        = countLinesBeforeBody(pre);
        String entityCode = pre + body + " \n" + segments.post();
        // Expend any tabs to spaces the way python reads them to avoid mixing tabs and spaces
        entityCode = Indentation.expandLeadingTabs(entityCode);
        // Kill those damn \160 chars, which are non-breaking spaces
        entityCode = entityCode.replace('\u00A0', ' ');

        File workspace = new File(tempFolder, runName + "_" + sf.getName().replaceAll("[^a-zA-Z0-9]", "_"));
        // noinspection ResultOfMethodCallIgnored
        workspace.mkdirs();

        File entityFile = new File(workspace, "Entity.py");
        File mainFile   = new File(workspace, "Main.py");

        String executable = mainFile.toPath().toString();
        // The correction is not shifted: its body is the raw entity span, which does not start at the first line of the editor
        lineShifts.put(executable, whatToCompile == StudentOrCorrection.STUDENT ? offset : 0);

        String mainContent = "import sys\n" + "from Remote import connect\n" + "from Entity import run\n" + "\n" + "connect(sys.argv[1])\n" + "run()\n";

        Files.writeString(entityFile.toPath(), entityCode);
        Files.writeString(mainFile.toPath(), mainContent);

        // No compilation step for Python: syntax errors only surface when the process actually runs. Do a quick
        // "python3 -m py_compile" pass here so compile-time errors are reported at compile time, matching the other
        // languages' UX, instead of silently at first run.
        try {
          Process proc = new ProcessBuilder("python3", "-m", "py_compile", "Entity.py").directory(workspace).redirectErrorStream(true).start();
          String output = new BufferedReader(new InputStreamReader(proc.getInputStream())).lines().collect(Collectors.joining("\n"));
          output        = shiftLines(ENTITY_LOCATION, output, executable);
          int retcode   = proc.waitFor();
          if (retcode != 0) {
            PLMCompilerException e = new PLMCompilerException("Compiling " + entityFile.toString() + " yielded the following output:\n" + output,
                                                              Set.of(entityFile.toString()), new Error());
            exo.lastResult         = RunOutcome.newCompilationError(this, e.getMessage());
            if (out != null)
              out.log(e.getMessage());
            throw e;
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }

        mainPath = executable;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return mainPath;
  }

  /** Locations before the body, and all of them when debugging is enabled, are left untouched. */
  @Override protected String shiftLocations(String line, String executable) { return shiftLines(ENTITY_LOCATION, line, executable); }

  /** Runs "python3 &lt;executable&gt; &lt;socketPath&gt;" from the executable's own directory, with ENTITIES_DIR ahead of the user's PYTHONPATH. */
  @Override protected ProcessBuilder buildProcess(String executable, Path socketPath) throws IOException
  {
    File exec = new File(executable);
    if (!exec.exists())
      throw new RuntimeException(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));

    ProcessBuilder pb = new ProcessBuilder("python3", exec.getName(), socketPath.toString());
    pb.directory(exec.getParentFile());
    pb.environment().merge("PYTHONPATH", ENTITIES_DIR.toString(), (user, ours) -> ours + File.pathSeparator + user);
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

      String name = method.name();

      String argsStr = getParameterNames(method).stream().map(s -> ", " + s).collect(Collectors.joining());
      String command = "    sendCommand(\"" + name + "\"" + argsStr + ")";

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
