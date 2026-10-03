package plm.core.lang;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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
 * needs no compilation step at all (just write the .py files and spawn "python3 Main.py <socket>"), and no
 * import-rewriting for its copied support files (ValueSerializer.py, RecList.py):
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

  public String getRemotePythonFile(String remoteName) { return loadRemoteFile(remoteName, "python", ".py"); }

  /**
   * The editor content is flush left, but the body sits {@code indent} spaces deep in the template: python needs it to be indented
   * accordingly. Code that is already indented (saved by a previous version) keeps its relative indentation.
   */
  private static String reindent(String codeWithTabs, int indent)
  {
    String code = Indentation.expandLeadingTabs(codeWithTabs);
    int common  = Integer.MAX_VALUE;
    for (String line : code.split("\n")) {
      if (line.isBlank())
        continue;
      int len = 0;
      while (len < line.length() && line.charAt(len) == ' ')
        len++;
      common = Math.min(common, len);
    }
    if (common == Integer.MAX_VALUE)
      return code;

    StringBuilder sb = new StringBuilder();
    String[] lines   = code.split("\n", -1);
    for (int i = 0; i < lines.length; i++) {
      if (i > 0)
        sb.append("\n");
      if (!lines[i].isBlank())
        sb.append(" ".repeat(indent)).append(lines[i].substring(Math.min(common, lines[i].length())));
      else
        sb.append(lines[i]);
    }
    return sb.toString();
  }

  @Override public String compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {
    String runName = packageNameForExercise(exo, whatToCompile);

    String mainPath = null;

    try {
      for (SourceFile sf : exo.getSourceFilesList(this)) {
        String remote = sf.getRemote();

        List<String> extraSourcePaths = remoteExtraSourceFiles.getOrDefault(remote, List.of());
        StringBuilder extraImports    = new StringBuilder();
        for (String sourcePath : extraSourcePaths)
          extraImports.append("from ").append(fileNameWithoutExtension(sourcePath)).append(" import *\n");

        String imports    = "from ValueSerializer import *\nfrom Remote import *\n" + extraImports;
        String template   = imports + "\n\n" + (whatToCompile == StudentOrCorrection.CORRECTION ? sf.getCorrectionTemplate() : sf.getTemplate());
        String body       = whatToCompile == StudentOrCorrection.CORRECTION ? sf.getCorrectionBody() : reindent(sf.getBody(), sf.getBodyIndent());
        // The tabs of the entity and of the student are expanded the way python reads them, so that they never get mixed up with spaces
        String entityCode = Indentation.expandLeadingTabs(sf.getCompilableContent(template, body).content());

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

        mainPath = mainFile.toPath().toString();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return mainPath;
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
