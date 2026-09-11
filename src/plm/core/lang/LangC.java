package plm.core.lang;

import java.awt.Color;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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

public class LangC extends TemplatedRemoteLang {
  /* Language detection logic */
  private static String brokenLanguageMessage;
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;

  /**
   * Root directory for every temp file this language produces (compiled binaries and their generated .c source),
   * nested under the shared {@link RemoteExecutionLang#TMP_ROOT} like every other language's own tempFolder.
   */
  private static final Path C_ROOT = TMP_ROOT.resolve("C");

  /**
   * Where compiled objects for the fixed (student-independent) C sources are cached across exercises and PLM runs;
   *  see ensureCachedObject() below.
   */
  private static final Path OBJECTS_DIR = C_ROOT.resolve("objects");

  public LangC() { super("C", "c", ResourcesCache.getIcon("img/lang_c.png")); }

  @Override public String getBrokenLanguageMessage() { return brokenLanguageMessage; }

  @Override public boolean isBrokenLanguage()
  {
    Runtime runtime = Runtime.getRuntime();

    if (brokenLanguageState == BrokenLanguageState.Unitialized) {
      try {
        runtime.exec("gcc --version");
        brokenLanguageState = BrokenLanguageState.Usable;
      } catch (IOException e) {
        brokenLanguageMessage = e.getLocalizedMessage();
        e.printStackTrace();
        brokenLanguageState = BrokenLanguageState.NotUsable;
      }
    }
    return brokenLanguageState != BrokenLanguageState.Usable;
  }

  @Override public boolean isC() { return true; }

  @Override public void compileExo(Exercise exo, LogWriter out, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {

    List<SourceFile> sfs = exo.getSourceFilesList(this);
    if (sfs.isEmpty()) {
      String msg = exo.getName() + ": No source to compile";
      System.err.println(msg);
      exo.lastResult = RunOutcome.newCompilationError(msg);
      throw new PLMCompilerException(msg, null, null);
    }

    for (SourceFile sf : sfs) {
      String code     = sf.getCompilableContent(runtimePatterns, whatToCompile);
      String execPath = compile(code, exo.getId(), exo, whatToCompile);
      sf.meta.put("C", execPath);
    }
  }

  /**
   * Compile the given code and return the absolute path of the resulting executable.
   *
   * Each call gets its own fresh temp directory (holding the generated .c file and the executable), instead of a path
   * deterministically derived from exo.getId() alone: compileExo() is called separately -- and not necessarily in
   * lockstep with runEntity() -- for the student's code and for the teacher's correction, so a shared, overwritable
   * path would let one clobber the other's executable between "compile" and "run" (e.g. the student's entity ending up
   * silently running the correction's code, or vice versa).
   *
   * The fixed C sources (the wire-protocol/serialization glue, common to all universes, and the universe-specific
   * glue) never depend on the student's own code, so compiling them fresh on every single run would be wasted work;
   * ensureCachedObject() below compiles each one to a .o at most once (across every exercise and every PLM run) and
   * reuses it afterwards. Only the student/correction file itself is compiled anew every time, then linked against
   * those cached objects.
   */
  private String compile(String code, String executable, Exercise exo, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {
    try {
      Files.createDirectories(C_ROOT);
      Path compileDir = Files.createTempDirectory(C_ROOT, exo.getId() + "-" + whatToCompile + "-");

      boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
      File exec         = new File(compileDir.toFile(), executable + (isWindows ? ".exe" : ""));

      String remote = getRemoteOrFail(code, "C", exo, null);

      String valueSerializerH = readResource("value_serializer.h");
      String valueSerializerC = readResource("value_serializer.c");
      String remoteH          = readResource("Remote.h");
      String remoteC          = readResource("Remote.c");
      String remoteWorldH     = loadRemoteFile(remote, "c", ".h");
      String remoteWorldC     = loadRemoteFile(remote, "c", ".c");

      Path valueSerializerObj = ensureCachedObject("value_serializer", valueSerializerC, Map.of("value_serializer.h", valueSerializerH), isWindows);
      Path remoteObj          = ensureCachedObject("Remote", remoteC, Map.of("Remote.h", remoteH), isWindows);
      Path remoteWorldObj     = ensureCachedObject(remote, remoteWorldC, Map.of("Remote.h", remoteH, remote + ".h", remoteWorldH), isWindows);

      // These two headers still need a real, physical presence in compileDir: the student/correction file below
      // #include's them directly, unlike value_serializer.h which nothing outside of value_serializer.c itself needs.
      Files.writeString(compileDir.resolve("Remote.h"), remoteH);
      Files.writeString(compileDir.resolve(remote + ".h"), remoteWorldH);

      // The student/correction code never declares these includes itself (it never had to, back when everything was
      // flattened into one file where Remote.h's declarations were already visible by construction); provide them here.
      // Some exercises' correction code *does* contain its own local #include lines, pointing at wherever that header
      // lives in the PLM source tree (a convenience so external editors can resolve symbols outside of PLM) -- those
      // paths mean nothing in compileDir, so: rewrite the ones referring to a header we actually placed here down to
      // a plain local filename (harmless to include twice, thanks to their include guards), and drop any other local
      // include we don't recognize, since we have no way to resolve it here either.
      Set<String> knownHeaders = Set.of("Remote.h", "value_serializer.h", remote + ".h");
      String studentCode       = Arrays.stream(code.split("\n", -1))
                               .map(codeLine -> rewriteOrDropLocalInclude(codeLine, knownHeaders))
                               .filter(java.util.Objects::nonNull)
                               .collect(Collectors.joining("\n"));

      String studentFileName = exo.getId() + ".c";
      Files.writeString(compileDir.resolve(studentFileName), "#include \"Remote.h\"\n#include \"" + remote + ".h\"\n\n" + studentCode);

      String linkInputs = studentFileName + " \"" + valueSerializerObj + "\" \"" + remoteObj + "\" \"" + remoteWorldObj + "\"";
      String linkCmd    = "gcc -g -Wall -lm -lpthread " + (isWindows ? "-lws2_32 " : "") + "-fsanitize=address -o \"" + exec + "\" " + linkInputs;
      //  -O0 -fno-omit-frame-pointer

      String errors = runShellCommand(linkCmd, compileDir.toFile(), isWindows);
      if (!errors.isEmpty()) {
        PLMCompilerException e = new PLMCompilerException(errors, null, null);
        if (Game.getInstance().isDebugEnabled())
          System.err.println(Game.i18n.tr("Compilation error. The linking command " + linkCmd + " failed:"));
        else
          System.err.println(Game.i18n.tr("Compilation error. The linking command failed:"));
        System.err.println(e.getMessage());
        System.err.println(code);

        exo.lastResult = RunOutcome.newCompilationError(e.getMessage());

        throw e;
      }

      return exec.getAbsolutePath();
    } catch (IOException ioe) {
      throw new PLMCompilerException(ioe.getMessage(), null, ioe, null);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new PLMCompilerException(e.getMessage(), null, e, null);
    }
  }

  /**
   * Compile a fixed (student-independent) C source to a .o file at most once, reusing it on every later call. Cached
   * objects are named after a hash of their own source content, not a fixed name: a PLM upgrade that changes one of
   * these bundled sources then simply produces a differently-named object instead of silently reusing a stale one --
   * the old, now-unreferenced object is just harmless orphaned disk usage, same as the per-compile directories above.
   *
   * headers lists every header (by filename) that baseName's own "#include" needs to find alongside it while it gets
   * compiled in isolation.
   *
   * Two concurrent first-uses of the same not-yet-cached object are safe either way (Files.move() is atomic, so neither
   * can observe or link against a half-written object file, and both would produce byte-identical output regardless);
   * the per-name lock below only avoids paying for gcc twice, it isn't needed for correctness.
   */
  private static final ConcurrentHashMap<String, Object> objectLocks = new ConcurrentHashMap<>();

  private static Path ensureCachedObject(String baseName, String sourceContent, Map<String, String> headers, boolean isWindows)
      throws IOException, InterruptedException, PLMCompilerException
  {
    String hash     = Integer.toHexString(sourceContent.hashCode());
    Path objectFile = OBJECTS_DIR.resolve(baseName + "-" + hash + ".o");
    if (Files.exists(objectFile))
      return objectFile;

    Object lock = objectLocks.computeIfAbsent(objectFile.toString(), k -> new Object());
    synchronized (lock) {
      if (Files.exists(objectFile)) // another thread may have just finished compiling it while we waited for the lock
        return objectFile;

      Files.createDirectories(OBJECTS_DIR);
      Path scratch = Files.createTempDirectory(OBJECTS_DIR, baseName + "-build-");
      try {
        for (Map.Entry<String, String> header : headers.entrySet())
          Files.writeString(scratch.resolve(header.getKey()), header.getValue());
        Path sourceFile = scratch.resolve(baseName + ".c");
        Files.writeString(sourceFile, sourceContent);

        Path tmpObject = scratch.resolve(baseName + ".o");
        // Must share -fsanitize=address with the final link step: ASan's instrumentation needs to be consistent across
        // every object file being linked together. -Wall/-g are harmless either way; -lm/-lpthread/-lws2_32 are link-only
        // flags, meaningless here since we're not linking anything yet.
        String compileCmd = "gcc -g -x c -Wall -fsanitize=address -c -o \"" + tmpObject + "\" \"" + sourceFile + "\"";
        String errors     = runShellCommand(compileCmd, scratch.toFile(), isWindows);
        if (!errors.isEmpty())
          throw new PLMCompilerException(errors, null, null);

        Files.move(tmpObject, objectFile, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
      } finally {
        try (var walk = Files.walk(scratch)) {
          walk.sorted(Comparator.reverseOrder()).forEach(p -> {
            try {
              Files.delete(p);
            } catch (IOException ignored) {
            }
          });
        }
      }
    }
    return objectFile;
  }

  /**
   * Run a shell command from workDir, returning everything it printed on stdout+stderr (empty = success, by
   *  convention of every caller here: gcc stays silent unless something went wrong).
   */
  private static String runShellCommand(String shellCommand, File workDir, boolean isWindows) throws IOException, InterruptedException
  {
    String[] arg1 = isWindows ? new String[] {"cmd.exe", "/c", shellCommand} : new String[] {"/bin/sh", "-c", shellCommand};

    Process process        = Runtime.getRuntime().exec(arg1, null, workDir);
    final StringBuffer out = new StringBuffer();

    Thread stdout = drain(process.getInputStream(), out);
    Thread stderr = drain(process.getErrorStream(), out);
    stdout.start();
    stderr.start();
    process.waitFor();
    stdout.join();
    stderr.join();

    return out.toString();
  }

  private static Thread drain(InputStream in, StringBuffer into)
  {
    return new Thread() {
      public void run()
      {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
          String line;
          while ((line = reader.readLine()) != null)
            into.append(line + "\n");
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      }
    };
  }

  /**
   * Read a classloader resource from "resources/langages/c/" as a String. Used for the fixed C sources that don't fit
   *  loadRemoteFile()'s "Remote"-prefixed naming convention.
   */
  private static String readResource(String fileName) throws IOException { return readClasspathResource("resources/langages/c/" + fileName); }

  /*
   * If {@code line} is a local #include (e.g. from a correction file that also carries its own #include pointing deep
   * into the PLM source tree, so external editors can resolve symbols outside of PLM), reduce it to a plain filename
   * when that filename is one of {@code knownHeaders} we actually placed in the compile directory (harmless to
   * #include twice, they all have include guards), or drop the line entirely when it points somewhere we can't
   * resolve. Any other line (system includes, or plain code) is returned unchanged.
   */
  private static String rewriteOrDropLocalInclude(String line, Set<String> knownHeaders)
  {
    if (!line.startsWith("#include \""))
      return line;

    int firstQuote = line.indexOf('"');
    int lastQuote  = line.lastIndexOf('"');
    if (lastQuote <= firstQuote)
      return line; // malformed; leave it as-is rather than guess

    String includedPath = line.substring(firstQuote + 1, lastQuote);
    String basename     = includedPath.substring(includedPath.lastIndexOf('/') + 1);

    return knownHeaders.contains(basename) ? "#include \"" + basename + "\"" : null;
  }

  /**
   * Runs the compiled executable directly (its path is exactly what compileExo() produced, stored in
   * sf.meta.get("C") and copied onto the entity by Exercise.mutateEntities()), redirecting ASan's reports to a
   * file instead of stderr so they can be told apart from the student code's own stderr output and surfaced
   * separately (see onProcessFinished() below).
   */
  @Override protected ProcessBuilder buildProcess(String executable, Path socketPath) throws IOException
  {
    File exec = new File(executable);
    if (!exec.exists() || !exec.canExecute() || !exec.isFile())
      throw new IOException(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));

    ProcessBuilder pb = new ProcessBuilder(executable, socketPath.toString());
    // log_path=<...>/asan_report.txt ~~> don't report to stderr but to that file (ASan appends ".$PID" itself)
    // to_syslog=0   ~~> Prevent ASan from writing also to stderr
    pb.environment().put("ASAN_OPTIONS", "log_path=" + asanReportPath(exec) + ":to_syslog=0");
    return pb;
  }

  private static String asanReportPath(File exec) { return new File(exec.getParentFile(), "asan_report.txt").getAbsolutePath(); }

  /**
   * If gcc's Address Sanitizer detected an issue, its report lands next to the executable (see buildProcess() above)
   *  instead of stderr; surface it the same way a normal stderr line would be.
   */
  @Override protected void onProcessFinished(Process process, String executable, RunOutcome progress)
  {
    File report = new File(asanReportPath(new File(executable)) + "." + process.pid());
    if (!report.exists())
      return;

    System.err.println(Game.i18n.tr("The Address Sanitizer detected an issue with the execution of your entity. You probably want to fix "
                                    + "it.\nThe exact error message contains hints about the problem. Good luck in debugging this.\n"));
    try (BufferedReader br = new BufferedReader(new FileReader(report))) {
      String line;
      while ((line = br.readLine()) != null)
        System.err.println(line);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    report.delete();
  }

  public static class LangCExternalPrimitiveGenerator implements ExternalPrimitiveLanguage {

    String getLanguageType(Class<?> type)
    {
      if (type == Color.class)
        return "Color";
      if (type == Direction.class)
        return "Direction";
      if (type == Point.class)
        return "Point";
      if (type == Point[].class)
        return "PointArray";
      if (type == Double.class || type == double.class)
        return "double";
      if (type == Integer.class || type == int.class)
        return "int";
      if (type == String.class)
        return "char*";
      if (type == Character.class || type == char.class)
        return "char";
      if (type == Boolean.class || type == boolean.class)
        return "int";
      if (type == void.class || type == Void.class)
        return "void";

      throw new IllegalStateException("Unknown type: " + type);
    }

    String getTypeDeclaration(Class<?> type)
    {
      if (type == Direction.class) {
        return "typedef enum{\n"
            + "    NORTH,\n"
            + "    EAST,\n"
            + "    SOUTH,\n"
            + "    WEST\n"
            + "} Direction;";
      }
      if (type == Color.class) {
        return "// Explicit values, matching java.awt.Color.X.getRGB() exactly (signed 32-bit ARGB) so that\n"
            + "// get_answer_color() can just cast the received raw integer to this enum,\n"
            + "// without any lookup table that would prevent the use of arbitrary colors.\n"
            + "typedef enum{\n"
            + "    white = -1,\n"
            + "    black = -16777216,\n"
            + "    blue = -16776961,\n"
            + "    cyan = -16711681,\n"
            + "    darkGray = -12566464,\n"
            + "    gray = -8355712,\n"
            + "    green = -16711936,\n"
            + "    lightGray = -4144960,\n"
            + "    magenta = -65281,\n"
            + "    orange = -14336,\n"
            + "    pink = -20561,\n"
            + "    red = -65536,\n"
            + "    yellow = -256\n"
            + "} Color;";
      }
      if (type == Point.class) {
        return "typedef struct {\n"
            + "    double x;\n"
            + "    double y;\n"
            + "} Point;";
      }
      if (type == Point[].class) {
        // generate() sorts type declarations by name so that "Point" comes before "PointArray"
        return "typedef struct {\n"
            + "    Point* items;\n"
            + "    int count;\n"
            + "} PointArray;";
      }
      return "";
    }

    /**
     * C function bodies needed to read a value of these types off the wire.
     * Scalar types are already handled by Remote.c natively
     */
    String getTypeSupportFunctions(Class<?> type)
    {
      if (type == Color.class) {
        return "Color get_answer_color()\n"
            + "{\n"
            + "    const char* line = get_answer_line(); // e.g. \"C-65536\"\n"
            + "    return (Color)strtol(line + 1, NULL, 10); // +1: skip the leading 'C' tag\n"
            + "}";
      }
      if (type == Point.class) {
        return "Point get_answer_point()\n"
            + "{\n"
            + "    const char* line = get_answer_line(); // e.g. \"P1.500000:2.300000\"\n"
            + "    Point p;\n"
            + "    sscanf(line, \"P%lf:%lf\", &p.x, &p.y);\n"
            + "    return p;\n"
            + "}";
      }
      if (type == Point[].class) {
        return "PointArray get_answer_point_array()\n"
            + "{\n"
            + "    const char* line = get_answer_raw_line(); // e.g. \"P[3:P1.0:2.0:P4.0:5.0:P7.0:8.0]\"\n"
            + "    PointArray result;\n"
            + "\n"
            + "    const char* p = strchr(line, '[') + 1;\n"
            + "    result.count  = atoi(p);\n"
            + "    result.items  = malloc(sizeof(Point) * result.count);\n"
            + "\n"
            + "    p = strchr(p, ':') + 1; // skip past \"n:\"\n"
            + "    for (int i = 0; i < result.count; i++) {\n"
            + "        sscanf(p, \"P%lf:%lf\", &result.items[i].x, &result.items[i].y);\n"
            + "        p = strchr(p, ':') + 1; // skip \"P<x>:\"\n"
            + "        p = strchr(p, ':');     // land on the ':' right after <y> (NULL if this was the last element)\n"
            + "        if (p != NULL)\n"
            + "            p = p + 1;\n"
            + "    }\n"
            + "    return result;\n"
            + "}";
      }
      return "";
    }

    String getParameter(PrimitiveParameter parameter) { return getLanguageType(parameter.type()) + " " + parameter.name(); }

    String getPrototype(PrimitiveMethod method)
    {
      String name                         = method.name();
      List<PrimitiveParameter> parameters = method.parameters();
      Class<?> output                     = method.output();

      final String outputString = Optional.ofNullable(output).map(this::getLanguageType).orElse("void");

      return outputString + " " + name + "(" + parameters.stream().map(this::getParameter).collect(Collectors.joining(", ")) + ");";
    }

    String getReturning(Class<?> type)
    {
      if (type == null)
        return "";

      if (type == String.class)
        return "get_answer_string()";
      if (type == Double.class || type == double.class)
        return "get_answer_double()";
      if (type == Character.class || type == char.class)
        return "get_answer_char()";
      if (type == Color.class)
        return "get_answer_color()";
      if (type == Direction.class)
        return "get_answer_int()";
      if (type == Point.class)
        return "get_answer_point()";
      if (type == Point[].class)
        return "get_answer_point_array()";
      if (type == Integer.class || type == int.class)
        return "get_answer_int()";
      if (type == Boolean.class || type == boolean.class)
        return "get_answer_int()";
      if (type == void.class || type == Void.class)
        return "";

      throw new IllegalStateException("Unknown type: " + type);
    }

    String getTemplatingForType(Class<?> type)
    {
      // Each format embeds the literal wire tag ValueSerializer.serialize() would emit for this type,
      // matching the conventions of ValueSerializer.
      if (type == String.class)
        return "%s";
      if (type == Double.class || type == double.class)
        return "f%lf";
      if (type == Character.class || type == char.class)
        return "c%c";
      if (type == Color.class)
        return "C%d";
      if (type == Direction.class)
        return "i%d";
      if (type == Integer.class || type == int.class)
        return "i%d";
      if (type == Boolean.class || type == boolean.class)
        return "b%d";
      if (type == void.class || type == Void.class)
        return "";

      throw new IllegalStateException("Unknown type: " + type);
    }

    /** The C expression to pass to send_command() for this parameter. Itself except String, which gets quoted. */
    String getArgumentExpression(PrimitiveParameter parameter)
    {
      if (parameter.type() == String.class)
        return "escape_string(" + parameter.name() + ")";
      return parameter.name();
    }

    String getImplementation(PrimitiveMethod method)
    {
      String prototype = getPrototype(method);
      prototype        = prototype.substring(0, prototype.length() - 1);

      int id      = method.id();
      String name = method.name();
      int n          = method.parameters().size();
      String formats = method.parameters().stream().map(PrimitiveParameter::type).map(this::getTemplatingForType).collect(Collectors.joining(":"));

      // Wrapped as "[n:tag1fmt1:tag2fmt2:...]" (or just "[0]" with no params), matching exactly what
      // ValueSerializer.serialize(Object[] args) produces on the Java side -- see CommandExecutor.command(), which parses
      // this same shape for every language uniformly.
      String argsWire = "[" + n + (formats.isEmpty() ? "" : ":" + formats) + "]";

      String command = "\tsend_command(\"" + id + " " + argsWire + " " + name + "\"" +
                       method.parameters().stream().map(this::getArgumentExpression).map(s -> ", " + s).collect(Collectors.joining()) + ");";

      String returning = method.hasReturn() ? "\treturn " + getReturning(method.output()) + ";" : "";

      return prototype + "{\n" + command + "\n" + returning + "\n}";
    }

    @Override public void generate(File folder, String name, List<PrimitiveMethod> methods) throws IOException
    {
      Set<Class<?>> involved = ExternalPrimitiveLanguage.involved(methods);

      final String guard = name.toUpperCase() + "_H";

      final String header_prefix = "/* THIS FILE IS GENERATED. DO NOT EDIT */\n#ifndef " + guard + "\n"
                                   + "#define " + guard + "\n"
                                   + "\n"
                                   + "#include <stdio.h>\n"
                                   + "#include <stdlib.h>\n"
                                   + "#include <stdarg.h>\n"
                                   + "#include <string.h>";
      final String header_suffix = "#endif";

      // "Point" before "PointArray": PointArray's typedef references Point
      List<Class<?>> sortedInvolved = involved.stream().sorted(Comparator.comparing(this::getLanguageType)).toList();

      final String type_declarations = sortedInvolved.stream().map(this::getTypeDeclaration).filter(o -> !o.isBlank()).collect(Collectors.joining("\n\n"));

      final String prototypes = methods.stream().map(this::getPrototype).collect(Collectors.joining("\n"));

      final String header = String.join("\n\n", header_prefix, type_declarations, prototypes, header_suffix);

      final String type_support_functions =
          sortedInvolved.stream().map(this::getTypeSupportFunctions).filter(o -> !o.isBlank()).collect(Collectors.joining("\n\n"));

      final String implementations = methods.stream().map(this::getImplementation).collect(Collectors.joining("\n\n"));

      final String code = "/* THIS FILE IS GENERATED. DO NOT EDIT */\n#include \"Remote.h\"\n#include \"" + name + ".h\"\n\n" + type_support_functions +
                          "\n\n" + implementations;

      Files.writeString(new File(folder, name + ".h").toPath(), header);
      Files.writeString(new File(folder, name + ".c").toPath(), code);
    }
  }
}
