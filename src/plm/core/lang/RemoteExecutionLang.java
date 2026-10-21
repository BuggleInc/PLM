package plm.core.lang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import javax.swing.ImageIcon;
import plm.core.lang.primitives.ExternalPrimitiveLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.RunOutcome;
import plm.universe.CommandExecutor;
import plm.universe.Entity;

/**
 * Ancestor of every language whose student code runs as an external process, talking back to
 * {@link CommandExecutor} over a UNIX-domain-socket protocol (currently Java, Scala, Python and C).
 *
 * Factors two things common to all of them:
 * <ul>
 * <li>{@code compileExo} returns a textual reference to the compiled artifact (e.g. a jar or binary path), which the
 * caller threads through {@link #runEntity} as its {@code executable} parameter;</li>
 * <li>the part of {@link #runEntity} that is identical for all languages: binding the protocol socket, starting the
 * process, relaying its stdout/stderr, and running the command-reading loop that feeds student primitive calls to
 * {@link CommandExecutor}. The only thing that actually differs from one language to another is how to turn the
 * compiled/interpreted "script" reference into a runnable {@link ProcessBuilder}, which is left to {@link #buildProcess}.</li>
 * </ul>
 */
public abstract class RemoteExecutionLang extends ProgrammingLanguage {

  /**
   * Root directory under which every subclass keeps its own temporary files -- compiled artifacts and per-exercise
   * workspaces (see each language's own tempFolder/*_ROOT) -- so that cleaning up (or just inspecting) the PLM's scratch
   * space only ever means looking at a single "plm" directory instead of one spot per language.
   */
  protected static final Path TMP_ROOT = Path.of(System.getProperty("java.io.tmpdir"), "plm");

  public RemoteExecutionLang(String lang, String ext, ImageIcon i) { super(lang, ext, i); }

  /** The generator of the glue of this language, which also renders the signatures of the primitives. */
  public abstract ExternalPrimitiveLanguage getPrimitiveGenerator();

  /**
   * Build the process that will run the student code, given the {@code executable} value {@link #runEntity} received for this
   * language (typically a path produced by {@code compileExo}) and the TCP port on 127.0.0.1 of the protocol socket that the
   * process must connect to. Implementations are responsible for interpreting the "script" string as they see fit (a plain
   * path to an executable, a jarfile or a script, etc.) and for checking that whatever it points to actually exists.
   */
  protected abstract ProcessBuilder buildProcess(String executable, int port) throws IOException;

  /**
   * Optional per-language hook, called once the student process has exited (right after {@code process.waitFor()}
   * returns) and before the retcode/outcome bookkeeping below. No-op by default; overridden by languages that need to
   * inspect something left behind by the process itself -- e.g. C reads a separate ASan report file, named after the
   * process's PID, next to the executable.
   */
  protected void onProcessFinished(Process process, String executable, RunOutcome progress) {}

  /**
   * Optional per-language hook, called on every line the student process writes on its stderr, before it is printed and
   * captured. Returns the line to use instead, e.g. with the code locations of a stack trace translated back to the editor. The
   * line is returned as is by default.
   */
  protected String shiftLocations(String line, String executable) { return line; }

  @Override public void runEntity(final Entity ent, final RunOutcome progress, final String executable)
  {
    final StringBuffer resEvaluationError = new StringBuffer();

    try {
      if (executable == null)
        throw new IllegalStateException("It seems that the code was not compiled. I am puzzled.");

      // Captured (not just printed) so that if the process never connects at all, its stdout/stderr -- almost certainly
      // containing the actual reason (missing interpreter, exception thrown before Remote.connect() is even reached,
      // etc.) -- can be surfaced directly in the error message shown to the person, not just to the PLM server's own
      // console. These must start capturing right away, BEFORE the accept()/timeout dance below: starting them only
      // after a successful connection means a process that never connects is killed with its output never having been
      // read at all, discarding the one clue that explains why.
      final StringBuffer capturedStdout = new StringBuffer();
      final StringBuffer capturedStderr = new StringBuffer();
      final Process process;
      final Thread stdoutReader;
      final Thread stderrReader;
      final Socket protocolSocket;
      // Only on the IPv4 loopback, which no firewall filters and no other host can reach (see CONTRIBUTING.md).
      // The literal address avoids "localhost", which may resolve to ::1 on one side only.
      try (ServerSocket server = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
        process        = buildProcess(executable, server.getLocalPort()).start();
        stdoutReader   = capture(process, process.getInputStream(), System.out, capturedStdout, line -> line);
        stderrReader   = capture(process, process.getErrorStream(), System.err, capturedStderr, line -> shiftLocations(line, executable));
        protocolSocket = acceptConnection(server, process);
      }
      if (protocolSocket == null) {
        process.destroyForcibly();
        // Give the reader threads a moment to drain whatever the process had already written before being killed.
        try {
          stdoutReader.join(2000);
          stderrReader.join(2000);
        } catch (InterruptedException ignored) {
          Thread.currentThread().interrupt();
        }
        progress.outcome        = RunOutcome.kind.FAIL;
        String details          = (capturedStderr.length() > 0 ? capturedStderr.toString() : capturedStdout.toString()).strip();
        progress.executionError = Game.i18n.tr("Protocol connection failed: the program never connected to the PLM.") +
                                  (details.isEmpty() ? " (no output was produced by the child process at all -- check that it is on the PATH)"
                                                     : "\n\n--- child process output ---\n" + details);
        return;
      }

      protocolSocket.setTcpNoDelay(true); // each command is a short request awaiting its answer
      final BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(protocolSocket.getOutputStream(), StandardCharsets.UTF_8));
      final BufferedReader reader  = new BufferedReader(new InputStreamReader(protocolSocket.getInputStream(), StandardCharsets.UTF_8));

      // Tell, when the run is interrupted, whether the program was stuck or just long
      final AtomicInteger handledCommands       = new AtomicInteger();
      final AtomicReference<String> lastCommand = new AtomicReference<>();

      Thread commandReader = new Thread() {
        public void run()
        {
          boolean handling = false;
          try {
            String command;
            while ((command = reader.readLine()) != null) {
              lastCommand.set(command);
              handling = true;
              CommandExecutor.command(ent, command, bwriter);
              handling = false;
              handledCommands.incrementAndGet();
            }
          } catch (Exception e) {
            if (protocolSocket.isClosed()) // closed when interrupting the run below: not a protocol error
              return;
            System.err.println((handling ? "Error while handling the command " : "Error while reading the command after ") + lastCommand.get() + " (" +
                               handledCommands.get() + " commands handled so far)");
            e.printStackTrace();
            progress.outcome        = RunOutcome.kind.FAIL;
            progress.executionError = e.getMessage();
            process.destroyForcibly();
          }
        }
      };

      commandReader.start();

      int retcode;
      try {
        retcode = process.waitFor();
      } catch (InterruptedException ie) {
        // "Stop" interrupts this runner thread (see LessonRunner.stopAll()) to break out of an infinite student
        // loop. Being unblocked from waitFor() doesn't kill the child process by itself -- do that explicitly here,
        // the same way as the "process never connected" branch above; its reader threads see the pipes and the
        // socket close and exit on their own (EOF/IOException).
        process.destroyForcibly(); // before closing the socket, so that the process does not complain about it before dying
        protocolSocket.close();
        commandReader.interrupt(); // it may wait for the next step forever
        Thread.currentThread().interrupt();
        System.err.println("Run of " + executable + " interrupted after " + handledCommands.get() + " protocol commands, the last one being " +
                           lastCommand.get());
        progress.outcome = RunOutcome.kind.FAIL;
        progress.setExecutionError(Game.i18n.tr("You interrupted the execution, did you fall into an infinite loop ?\n"
                                                + "Your program must stop by itself to successfully pass the exercise.\n"));
        return;
      }

      stdoutReader.join();
      stderrReader.join();
      commandReader.join();

      onProcessFinished(process, executable, progress);

      bwriter.close();
      protocolSocket.close();

      if (retcode != 0)
        progress.setExecutionError("An issue occured in the executed code (return code: " + retcode + "). Check the output in the log panel for more info");

      if (resEvaluationError.length() > 0) {
        System.err.println(resEvaluationError.toString());
        progress.setCompilationError(resEvaluationError.toString());
      }

    } catch (Exception e) {
      if (e instanceof InterruptedException)
        Thread.currentThread().interrupt();
      resEvaluationError.append(e.getMessage());
      progress.setExecutionError(resEvaluationError.toString());
    }
  }

  /**
   * Waits for the process to connect to the server, for up to 10 seconds. Returns null if it does not, or if it ends before:
   * a student program that fails at startup is reported right away.
   */
  private static Socket acceptConnection(ServerSocket server, Process process) throws IOException
  {
    server.setSoTimeout(100);
    long deadline = System.nanoTime() + 10_000_000_000L;
    while (true) {
      boolean ended = !process.isAlive(); // checked before accept(), which still gets a connection made before the end
      try {
        return server.accept();
      } catch (SocketTimeoutException e) {
        if (ended || System.nanoTime() > deadline)
          return null;
      }
    }
  }

  /** Starts a thread copying the lines of an output of the process to echo and into, after transforming them */
  private static Thread capture(Process process, InputStream output, PrintStream echo, StringBuffer into, UnaryOperator<String> transform)
  {
    Thread reader = new Thread(() -> {
      try (BufferedReader lines = new BufferedReader(new InputStreamReader(output))) {
        String line;
        while ((line = lines.readLine()) != null) {
          line = transform.apply(line);
          echo.println(line);
          into.append(line).append("\n");
        }
      } catch (IOException ioe) {
        // Expected when process.destroyForcibly() tears down the pipes while this thread is blocked in readLine(). The process
        // then ends within milliseconds, but may still be alive when the exception arrives: only report the ones that last.
        try {
          // Wait a bit before printing the stack. The swing thread may interrupt us if the user pressed "Stop".
          if (!process.waitFor(1, TimeUnit.SECONDS))
            ioe.printStackTrace();
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
        }
      }
    });
    reader.start();
    return reader;
  }
}
