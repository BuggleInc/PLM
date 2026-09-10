package plm.core.lang;

import java.awt.Color;
import java.io.*;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import plm.universe.CommandExecutor;
import plm.universe.Direction;
import plm.universe.Entity;
import plm.universe.Point;

public class LangC extends ProgrammingLanguage {
  /* Language detection logic */
  private static String brokenLanguageMessage;
  private static BrokenLanguageState brokenLanguageState = BrokenLanguageState.Unitialized;

  /**
   * Root directory for every temp file this language produces (compiled binaries and their generated .c source).
   * It's in "plm/C", placed under "/tmp" on Linux/Mac, "C:\Users\...\AppData\Local\Temp" on Windows. ).
   */
  private static final Path TMP_ROOT = Path.of(System.getProperty("java.io.tmpdir"), "plm", "C");

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
    if (sfs == null || sfs.isEmpty()) {
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
   * Each call gets its own fresh temp directory (holding both the generated .c file and the executable), instead of a
   * path deterministically derived from exo.getId() alone: compileExo() is called separately -- and not necessarily in
   * lockstep with runEntity() -- for the student's code and for the teacher's correction, so a shared, overwritable
   * path would let one clobber the other's executable between "compile" and "run" (e.g. the student's entity ending up
   * silently running the correction's code, or vice versa).
   */
  private String compile(String code, String executable, Exercise exo, StudentOrCorrection whatToCompile) throws PLMCompilerException
  {

    Runtime runtime = Runtime.getRuntime();

    final StringBuffer resCompilationErr = new StringBuffer();
    try {
      Files.createDirectories(TMP_ROOT);
      Path compileDir = Files.createTempDirectory(TMP_ROOT, exo.getId() + "-" + whatToCompile + "-");

      String extension = "";
      String os        = System.getProperty("os.name").toLowerCase();
      if (os.indexOf("win") >= 0) {
        extension = ".exe";
      }

      File exec = new File(compileDir.toFile(), executable + extension);

      String remote = "";
      if (code.contains("RemoteBat"))
        remote = "RemoteBat";
      else if (code.contains(".cons."))
        remote = "RemoteCons";
      else if (code.contains("Buggle") || code.contains("Langton") || code.contains("Turmite"))
        remote = "RemoteBuggle";
      else if (code.contains("Turtle"))
        remote = "RemoteTurtle";
      else if (code.contains("Flag"))
        remote = "RemoteFlag";
      else if (code.contains("Baseball"))
        remote = "RemoteBaseball";
      else if (code.contains("Pancake"))
        remote = "RemotePancake";
      else if (code.contains("Hanoi"))
        remote = "RemoteHanoi";
      else if (code.contains("Sort"))
        remote = "RemoteSort";
      else {
        PLMCompilerException e = new PLMCompilerException("This universe is not implemented in C.", null, null);
        exo.lastResult         = RunOutcome.newCompilationError(e.getMessage());
        throw e;
      }

      String line;
      String compiled_code_name = new File(compileDir.toFile(), exo.getId() + ".c").getAbsolutePath();
      PrintWriter compiled_code = new PrintWriter(compiled_code_name);

      BufferedReader hSerializer =
          new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("resources/langages/c/value_serializer.h")));
      compiled_code.append("/**********************/\n");
      compiled_code.append("/* value_serializer.h */\n");
      compiled_code.append("/**********************/\n");
      while ((line = hSerializer.readLine()) != null)
        if (!line.startsWith("#include \""))
          compiled_code.append(line + "\n");
      hSerializer.close();

      BufferedReader cSerializer =
          new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("resources/langages/c/value_serializer.c")));
      compiled_code.append("/**********************/\n");
      compiled_code.append("/* value_serializer.c */\n");
      compiled_code.append("/**********************/\n");
      while ((line = cSerializer.readLine()) != null)
        if (!line.startsWith("#include \""))
          compiled_code.append(line + "\n");
      cSerializer.close();

      BufferedReader hRemote = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("resources/langages/c/Remote.h")));
      compiled_code.append("/************/\n");
      compiled_code.append("/* Remote.h */\n");
      compiled_code.append("/************/\n");
      while ((line = hRemote.readLine()) != null)
        if (!line.startsWith("#include \""))
          compiled_code.append(line + "\n");
      hRemote.close();

      BufferedReader cRemote = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("resources/langages/c/Remote.c")));
      compiled_code.append("/************/\n");
      compiled_code.append("/* Remote.c */\n");
      compiled_code.append("/************/\n");
      while ((line = cRemote.readLine()) != null)
        if (!line.startsWith("#include \""))
          compiled_code.append(line + "\n");
      cRemote.close();

      BufferedReader hRemoteWorld =
          new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("resources/langages/c/" + remote + ".h")));
      compiled_code.append("/****************/\n");
      compiled_code.append("/* " + remote + ".h */\n");
      compiled_code.append("/****************/\n");
      while ((line = hRemoteWorld.readLine()) != null)
        if (!line.equals("#include \"Remote.h\""))
          compiled_code.append(line + "\n");
      hRemoteWorld.close();

      BufferedReader cRemoteWorld =
          new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("resources/langages/c/" + remote + ".c")));
      compiled_code.append("/****************/\n");
      compiled_code.append("/* " + remote + ".c */\n");
      compiled_code.append("/****************/\n");
      while ((line = cRemoteWorld.readLine()) != null)
        if (!line.startsWith("#include \""))
          compiled_code.append(line + "\n");
      cRemoteWorld.close();

      compiled_code.append("/****************/\n");
      compiled_code.append("/* Student code */\n");
      compiled_code.append("/****************/\n");
      for (String li : code.split("\n"))
        if (!li.startsWith("#include \""))
          compiled_code.append(li + "\n");
      compiled_code.close();

      String[] arg1;
      if (os.indexOf("win") >= 0) {
        arg1    = new String[3];
        arg1[0] = "cmd.exe";
        arg1[1] = "/c";
        arg1[2] = "gcc -g -x c -Wall -lm -lpthread -lws2_32 -fsanitize=address -o \"" + exec + "\" " + compiled_code_name;
      } else {
        arg1    = new String[3];
        arg1[0] = "/bin/sh";
        arg1[1] = "-c";
        arg1[2] = "gcc -g -x c -Wall -lm -lpthread -fsanitize=address -o \"" + exec + "\" " + compiled_code_name;
        //  -O0 -fno-omit-frame-pointer
      }

      final Process process        = runtime.exec(arg1);
      final BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      bwriter.write(compiled_code.toString());
      bwriter.close();

      Thread reader = new Thread() {
        public void run()
        {
          try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line           = "";
            try {
              while ((line = reader.readLine()) != null) {
                resCompilationErr.append(line + "\n");
              }
            } finally {
              reader.close();
            }
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }
      };

      Thread error = new Thread() {
        public void run()
        {
          try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line           = "";
            try {
              while ((line = reader.readLine()) != null) {
                resCompilationErr.append(line + "\n");
              }
            } finally {
              reader.close();
            }
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }
      };
      reader.start();
      error.start();
      process.waitFor();
      reader.join();
      error.join();

      if (resCompilationErr.length() > 0) {
        PLMCompilerException e = new PLMCompilerException(resCompilationErr.toString(), null, null);
        System.err.println(Game.i18n.tr("Compilation error:"));
        System.err.println(e.getMessage());
        System.err.println(code);

        exo.lastResult = RunOutcome.newCompilationError(e.getMessage());

        throw e;
      }

      return exec.getAbsolutePath();
    } catch (IOException ioe) {
      throw new PLMCompilerException(ioe.getMessage(), null, null);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new PLMCompilerException(e.getMessage(), null, null);
    }
  }

  @Override public List<Entity> mutateEntities(Exercise exercise, List<Entity> old, StudentOrCorrection whatToMutate)
  {
    List<SourceFile> sourceFiles = exercise.getSourceFilesList(this);

    if (sourceFiles.size() != 1)
      throw new IllegalStateException("ToBeYetImplemented: Cannot differentiate entity scripts for now.");

    String path = sourceFiles.get(0).meta.get("C");
    if (path != null)
      for (Entity o : old)
        o.setScript(this, path);

    return old;
  }

  @Override public void runEntity(final Entity ent, final RunOutcome progress)
  {
    final StringBuffer resCompilationErr = new StringBuffer();

    try {

      String cmd = ent.getScript(this);
      if (cmd == null)
        throw new IllegalStateException("TOFIX");

      File exec = new File(cmd);
      if (!exec.exists() || !exec.canExecute() || !exec.isFile()) {
        System.err.println(Game.i18n.tr("Error, please recompile the exercise: {0} does not exist", exec.getName()));
        return;
      }

      Path socketDir                    = Files.createTempDirectory("plm-c-sock-");
      Path socketPath                   = socketDir.resolve("protocol.sock");
      ServerSocketChannel serverChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
      serverChannel.bind(UnixDomainSocketAddress.of(socketPath));
      serverChannel.configureBlocking(false);
      Selector selector = Selector.open();
      serverChannel.register(selector, SelectionKey.OP_ACCEPT);

      String asan_report = new File(exec.getParentFile(), "asan_report.txt").getAbsolutePath();
      ProcessBuilder pb  = new ProcessBuilder(cmd, socketPath.toString());
      // log_path=/tmp/plmTmp/asan_report.txt.$PID ~~> don't report to stderr but to that file
      // to_syslog=0   ~~> Prevent ASan from writing also to stderr
      pb.environment().put("ASAN_OPTIONS", "log_path=" + asan_report + ":to_syslog=0");
      final Process process = pb.start();
      long pid              = process.pid();

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

      final BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(Channels.newOutputStream(protocolChannel), StandardCharsets.UTF_8));

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
          } catch (IOException ioe) {
            ioe.printStackTrace();
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
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }
      };

      Thread commandReader = new Thread() {
        public void run()
        {
          BufferedReader reader = new BufferedReader(new InputStreamReader(Channels.newInputStream(protocolChannel), StandardCharsets.UTF_8));
          Exception parseError  = null;
          String str            = "";
          try {
            while ((str = reader.readLine()) != null)
              CommandExecutor.command(ent, str, bwriter);
          } catch (Exception e) {
            parseError              = e;
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

      process.waitFor();

      stdoutReader.join();
      stderrReader.join();
      commandReader.join();

      bwriter.close();
      protocolChannel.close();
      Files.deleteIfExists(socketPath);
      Files.deleteIfExists(socketDir);

      File asan_report_file = new File(asan_report + "." + pid);
      if (asan_report_file.exists()) {
        System.err.println(Game.i18n.tr("The Address Sanitizer detected an issue with the execution of your entity. You probably want to fix "
                                        + "it.\nThe exact error message contains hints about the problem. Good luck in debugging this.\n"));
        try (BufferedReader br = new BufferedReader(new FileReader(asan_report_file))) {
          String line;
          while ((line = br.readLine()) != null) {
            System.err.println(line);
          }
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
        asan_report_file.delete();
      }

      if (resCompilationErr.length() > 0) {
        System.err.println(resCompilationErr.toString());
        progress.setCompilationError(resCompilationErr.toString());
      }

    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
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

      final String type_declarations =
          involved.stream()
              .sorted(Comparator.comparing(this::getLanguageType)) // "Point" before "PointArray": PointArray's typedef references Point
              .map(this::getTypeDeclaration)
              .filter(o -> !o.isBlank())
              .collect(Collectors.joining("\n\n"));

      final String prototypes = methods.stream().map(this::getPrototype).collect(Collectors.joining("\n"));

      final String header = String.join("\n\n", header_prefix, type_declarations, prototypes, header_suffix);

      final String type_support_functions = involved.stream()
                                                .sorted(Comparator.comparing(this::getLanguageType))
                                                .map(this::getTypeSupportFunctions)
                                                .filter(o -> !o.isBlank())
                                                .collect(Collectors.joining("\n\n"));

      final String implementations = methods.stream().map(this::getImplementation).collect(Collectors.joining("\n\n"));

      final String code = "/* THIS FILE IS GENERATED. DO NOT EDIT */\n#include \"Remote.h\"\n#include \"" + name + ".h\"\n\n" + type_support_functions +
                          "\n\n" + implementations;

      Files.writeString(new File(folder, name + ".h").toPath(), header);
      Files.writeString(new File(folder, name + ".c").toPath(), code);
    }
  }
}
