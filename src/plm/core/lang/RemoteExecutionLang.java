package plm.core.lang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import plm.core.PLMCompilerException;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.StudentOrCorrection;
import plm.core.model.lesson.RunOutcome;
import plm.core.model.session.SourceFile;
import plm.universe.CommandExecutor;
import plm.universe.Entity;

/**
 * Ancestor of every language whose student code runs as an external process, talking back to
 * {@link CommandExecutor} over a UNIX-domain-socket protocol (currently Java, Scala, Python and C).
 *
 * Factors two things common to all of them:
 * <ul>
 * <li>how a compiled artifact's path travels from {@code compileExo} to {@code runEntity}: {@code compileExo} stores it
 * in {@code sf.meta.get(getLang().toUpperCase())} (e.g. "JAVA", "SCALA", "PYTHON"), and {@link #mutateEntities} copies
 * it onto the entities so {@code runEntity} knows what to spawn;</li>
 * <li>the part of {@link #runEntity} that is identical for all languages: binding the protocol socket, starting the
 * process, relaying its stdout/stderr, and running the command-reading loop that feeds student primitive calls to
 * {@link CommandExecutor}. The only thing that actually differs from one language to another is how to turn the
 * compiled/interpreted "script" reference into a runnable {@link ProcessBuilder}, which is left to {@link #buildProcess}.</li>
 * </ul>
 */
public abstract class RemoteExecutionLang extends ProgrammingLanguage {

  /**
   * Root directory under which every subclass keeps its own temporary files -- compiled artifacts and per-exercise
   * workspaces (see each language's own tempFolder/*_ROOT), as well as the protocol sockets bound below in
   * {@link #runEntity} -- so that cleaning up (or just inspecting) the PLM's scratch space only ever means looking at
   * a single "plm" directory instead of one spot per language plus a handful of unprefixed socket directories.
   */
  protected static final Path TMP_ROOT = Path.of(System.getProperty("java.io.tmpdir"), "plm");

  public RemoteExecutionLang(String lang, String ext, ImageIcon i) { super(lang, ext, i); }

  @Override public ArrayList<Entity> mutateEntities(Exercise exo, List<Entity> olds, StudentOrCorrection whatToMutate) throws PLMCompilerException
  {
    List<SourceFile> sourceFiles = exo.getSourceFilesList(this);

    if (sourceFiles.size() != 1)
      throw new IllegalStateException("ToBeYetImplemented: Cannot differentiate entity scripts for now.");

    String path = sourceFiles.get(0).meta.get(getLang().toUpperCase());
    if (path != null)
      for (Entity old : olds)
        old.setScript(this, path);

    return new ArrayList<>(olds);
  }

  /**
   * Build the process that will run the student code, given the value {@link Entity#getScript} returned for this
   * language (typically a path produced by {@code compileExo}) and the path of the protocol socket that the process
   * must connect to. Implementations are responsible for interpreting the "script" string as they see fit (a plain
   * executable path, a "jar|mainClass" pair, etc.) and for checking that whatever it points to actually exists.
   */
  protected abstract ProcessBuilder buildProcess(String executable, Path socketPath) throws IOException;

  /**
   * Optional per-language hook, called once the student process has exited (right after {@code process.waitFor()}
   * returns) and before the retcode/outcome bookkeeping below. No-op by default; overridden by languages that need to
   * inspect something left behind by the process itself -- e.g. C reads a separate ASan report file, named after the
   * process's PID, next to the executable.
   */
  protected void onProcessFinished(Process process, String executable, RunOutcome progress) {}

  @Override public void runEntity(final Entity ent, final RunOutcome progress)
  {
    final StringBuffer resEvaluationError = new StringBuffer();

    try {
      String executable = ent.getScript(this);
      if (executable == null)
        throw new IllegalStateException("TOFIX");

      Files.createDirectories(TMP_ROOT);
      Path socketDir                    = Files.createTempDirectory(TMP_ROOT, getExt() + "-sock-");
      Path socketPath                   = socketDir.resolve("protocol.sock");
      ServerSocketChannel serverChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
      serverChannel.bind(UnixDomainSocketAddress.of(socketPath));
      serverChannel.configureBlocking(false);
      Selector selector = Selector.open();
      serverChannel.register(selector, SelectionKey.OP_ACCEPT);

      final Process process = buildProcess(executable, socketPath).start();

      // Captured (not just printed) so that if the process never connects at all, its stdout/stderr -- almost certainly
      // containing the actual reason (missing interpreter, exception thrown before Remote.connect() is even reached,
      // etc.) -- can be surfaced directly in the error message shown to the person, not just to the PLM server's own
      // console. These must start capturing right away, BEFORE the accept()/timeout dance below: starting them only
      // after a successful connection means a process that never connects is killed with its output never having been
      // read at all, discarding the one clue that explains why.
      final StringBuffer capturedStdout = new StringBuffer();
      final StringBuffer capturedStderr = new StringBuffer();

      Thread stdoutReader = new Thread() {
        public void run()
        {
          try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            try {
              String str;
              while ((str = reader.readLine()) != null) {
                System.out.println(str);
                capturedStdout.append(str).append("\n");
              }
            } finally {
              reader.close();
            }
          } catch (IOException ioe) {
            // Expected when process.destroyForcibly() (below, or in the accept-timeout branch) tears down the
            // process's pipes while this thread is still blocked in readLine(): not a real failure in its own right.
            if (process.isAlive())
              ioe.printStackTrace(); // genuinely unexpected in that case, surface it
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
              while ((str = reader.readLine()) != null) {
                System.err.println(str);
                capturedStderr.append(str).append("\n");
              }
            } finally {
              reader.close();
            }
          } catch (IOException ioe) {
            if (process.isAlive())
              ioe.printStackTrace();
          }
        }
      };

      stdoutReader.start();
      stderrReader.start();

      final int ACCEPT_TIMEOUT_MS = 10000;
      selector.select(ACCEPT_TIMEOUT_MS);
      SocketChannel protocolChannel = serverChannel.accept();
      selector.close();
      serverChannel.close();

      if (protocolChannel == null) {
        process.destroyForcibly();
        // Give the reader threads a moment to drain whatever the process had already written before being killed.
        try {
          stdoutReader.join(2000);
          stderrReader.join(2000);
        } catch (InterruptedException ignored) {
          Thread.currentThread().interrupt();
        }
        Files.deleteIfExists(socketPath);
        Files.deleteIfExists(socketDir);
        progress.outcome        = RunOutcome.kind.FAIL;
        String details          = (capturedStderr.length() > 0 ? capturedStderr.toString() : capturedStdout.toString()).strip();
        progress.executionError = Game.i18n.tr("Protocol connection failed: the program never connected to the PLM.") +
                                  (details.isEmpty() ? " (no output was produced by the child process at all -- check that it is on the PATH)"
                                                     : "\n\n--- child process output ---\n" + details);
        return;
      }

      final SocketChannel finalProtocolChannel = protocolChannel;
      final BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(Channels.newOutputStream(finalProtocolChannel), StandardCharsets.UTF_8));

      Thread commandReader = new Thread() {
        public void run()
        {
          BufferedReader reader = new BufferedReader(new InputStreamReader(Channels.newInputStream(finalProtocolChannel), StandardCharsets.UTF_8));
          Exception parseError  = null;
          String str            = "";
          try {
            while ((str = reader.readLine()) != null)
              CommandExecutor.command(ent, str, bwriter);
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

      commandReader.start();

      int retcode = process.waitFor();

      stdoutReader.join();
      stderrReader.join();
      commandReader.join();

      onProcessFinished(process, executable, progress);

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
}
