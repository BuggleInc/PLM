package plm.universe.posix;

import java.nio.charset.StandardCharsets;
import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Glue;
import plm.core.model.Game;
import plm.universe.Entity;
import plm.universe.ExplainedException;

/** The student's process. Its system calls are executed by the kernel of its world, which keeps the data as one char per byte. */
@Glue(value = "RemotePosix", wrapped = true,
      replaces = {"java.net.Socket", "java.net.ServerSocket", "java.lang.Thread", "java.io.FileInputStream", "java.io.FileOutputStream", "java.io.FileReader",
                  "java.io.FileWriter", "java.io.PrintWriter", "java.nio.file.Files"})
@EntityPrimitives(PosixEntityPrimitives.class)
public class PosixEntity extends Entity implements PosixEntityPrimitives {

  /** The kernel side of this process */
  private PosixProcess process() { return ((PosixWorld)getWorld()).student; }

  /** The kernel of the world, once the peers acted: they progress before each system call, and after it (see step()) */
  private PosixWorld kernel()
  {
    PosixWorld kernel = (PosixWorld)getWorld();
    kernel.advancePeers();
    return kernel;
  }

  @Override public void run() { /* To be overwritten */ }

  @Override public int socket(int domain, int socket_type, int protocol)
  {
    String arguments =
        (domain == PosixWorld.AF_INET ? "AF_INET" : domain) + ", " + (socket_type == PosixWorld.SOCK_STREAM ? "SOCK_STREAM" : socket_type) + ", " + protocol;
    return step("socket", arguments, kernel().socket(process(), domain, socket_type, protocol));
  }
  @Override public int bind(int fd, String ip, int port) { return step("bind", fd + ", " + ip + ":" + port, kernel().bind(process(), fd, ip, port)); }
  @Override public int listen(int fd, int backlog) { return step("listen", fd + ", " + backlog, kernel().listen(process(), fd, backlog)); }
  @Override public int accept(int fd) { return step("accept", String.valueOf(fd), kernel().accept(process(), fd)); }
  @Override public int connect(int fd, String ip, int port) { return step("connect", fd + ", " + ip + ":" + port, kernel().connect(process(), fd, ip, port)); }
  @Override public int send(int fd, byte[] data, int flags)
  {
    return step("send", fd + ", " + excerpt(data) + ", " + data.length + ", " + flags, kernel().send(process(), fd, text(data), flags));
  }
  @Override public byte[] recv(int fd, int len, int flags)
  {
    String data = kernel().recv(process(), fd, len, flags);
    return bytes(step("recv", fd + ", " + PosixWorld.filled(data) + ", " + len + ", " + flags, data));
  }
  @Override public int shutdown(int fd, int how)
  {
    String shown = how >= PosixWorld.SHUT_RD && how <= PosixWorld.SHUT_RDWR ? new String[] {"SHUT_RD", "SHUT_WR", "SHUT_RDWR"}[how] : String.valueOf(how);
    return step("shutdown", fd + ", " + shown, kernel().shutdown(process(), fd, how));
  }
  @Override public int open(String path, int flags)
  {
    String shown = new String[] {"O_RDONLY", "O_WRONLY", "O_RDWR", String.valueOf(flags & 3)}[flags & 3];
    shown += ((flags & PosixWorld.O_CREAT) != 0 ? "|O_CREAT" : "") + ((flags & PosixWorld.O_TRUNC) != 0 ? "|O_TRUNC" : "") +
             ((flags & PosixWorld.O_APPEND) != 0 ? "|O_APPEND" : "");
    return step("open", PosixWorld.quoted(path) + ", " + shown, kernel().open(process(), path, flags));
  }
  @Override public byte[] read(int fd, int len)
  {
    String data = kernel().read(process(), fd, len);
    return bytes(step("read", fd + ", " + PosixWorld.filled(data) + ", " + len, data));
  }
  @Override public int write(int fd, byte[] data)
  {
    return step("write", fd + ", " + excerpt(data) + ", " + data.length, kernel().write(process(), fd, text(data)));
  }
  @Override public int close(int fd) { return step("close", String.valueOf(fd), kernel().close(process(), fd)); }

  @Override public int unlink(String path) { return step("unlink", PosixWorld.quoted(path), kernel().unlink(process(), path)); }
  @Override public int sleep(int seconds) { return step("sleep", String.valueOf(seconds), kernel().sleep(seconds)); }
  @Override public String lastError() { return process().errno; }

  /** The data as kept by the kernel. Only ASCII is accepted for now, to not mix the encodings with the system calls. */
  private static String text(byte[] data)
  {
    for (byte b : data)
      if (b < 0) // a byte above 127
        throw new ExplainedException(Game.i18n.tr("This world only accepts ASCII data, without accented letters nor other special characters."));
    return new String(data, StandardCharsets.US_ASCII);
  }

  /** The bytes of the kernel's data, or null if the call failed */
  private static byte[] bytes(String data) { return data == null ? null : data.getBytes(StandardCharsets.ISO_8859_1); }

  /** The data in a buffer, as displayed by strace (or ... if the call failed and left it unchanged) */
  private static String excerpt(byte[] data) { return data == null ? "..." : PosixWorld.excerpt(new String(data, StandardCharsets.ISO_8859_1)); }

  /** Ends the trace as strace does, with the exit code of the process, or the signal killing it */
  @Override public void processEnded(Integer exitCode)
  {
    PosixWorld kernel = (PosixWorld)getWorld(); // without letting the peers act, as the process cannot see them anymore
    kernel.log("you", exitCode == null ? "+++ killed by SIGKILL +++" : "+++ exited with " + exitCode + " +++");
    kernel.notifyWorldUpdatesListeners();
  }

  /** Animates a step of the other processes or of the network, which happens while this process runs or waits */
  void animate() { stepUI(); }

  /** Traces the call in the style of strace, animates it, and lets the peers react to it before returning its result */
  private <T> T step(String call, String arguments, T result)
  {
    PosixWorld kernel = (PosixWorld)getWorld();
    kernel.log("you", call + "(" + arguments + ") = " + process().shown(result));
    stepUI();
    kernel.advancePeers();
    return result;
  }
}
