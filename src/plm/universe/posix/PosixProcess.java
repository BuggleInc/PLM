package plm.universe.posix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The kernel side of a process of the world (the student's one, or the one of a peer): its descriptor table, the data of its
 * standard streams and its last error. The system calls of PosixWorld act on the process that calls them.
 */
final class PosixProcess {
  private static final Map<String, String> STRERROR = Map.of(
      "EBADF", "Bad file descriptor", "ENOENT", "No such file or directory", "EINVAL", "Invalid argument", "ENOTSOCK", "Socket operation on non-socket",
      "ENOTCONN", "Transport endpoint is not connected", "ECONNREFUSED", "Connection refused", "EADDRINUSE", "Address already in use", "EISCONN",
      "Transport endpoint is already connected", "EAFNOSUPPORT", "Address family not supported by protocol", "EPROTONOSUPPORT", "Protocol not supported");

  /** The address of the machine of the process */
  final String ip;
  /** Whether the network transmits the data sent by this process only when its script says so (see Peer), instead of at once */
  final boolean scriptedNetwork;
  /** The descriptor table, a null slot being a free descriptor */
  final List<FileDescription> fds;
  /** The standard streams, on the descriptors 0, 1 and 2 at first, kept here for the comparison even once closed */
  final StandardStream stdin, stdout, stderr;
  /** The name of the last error, such as "ECONNREFUSED" */
  String errno = "";

  PosixProcess(String ip, boolean scriptedNetwork)
  {
    this.ip              = ip;
    this.scriptedNetwork = scriptedNetwork;
    stdin                = new StandardStream("stdin", true);
    stdout               = new StandardStream("stdout", false);
    stderr               = new StandardStream("stderr", false);
    fds                  = new ArrayList<>(List.of(stdin, stdout, stderr));
  }

  /** Copies other, its descriptions being copied in copies (see FileDescription.copy()) */
  PosixProcess(PosixProcess other, Map<FileDescription, FileDescription> copies)
  {
    ip              = other.ip;
    scriptedNetwork = other.scriptedNetwork;
    stdin           = other.stdin.copy(copies);
    stdout          = other.stdout.copy(copies);
    stderr          = other.stderr.copy(copies);
    errno           = other.errno;
    fds             = new ArrayList<>();
    for (FileDescription o : other.fds)
      fds.add(o == null ? null : o.copy(copies));
  }

  /** Records the error of a failing call, and returns -1 */
  int fail(String error)
  {
    errno = error;
    return -1;
  }

  /** Puts the object in the lowest free descriptor, and returns it */
  int allocate(FileDescription o)
  {
    int fd = fds.indexOf(null);
    if (fd < 0) {
      fds.add(o);
      return fds.size() - 1;
    }
    fds.set(fd, o);
    return fd;
  }

  /** The object of a descriptor, or null if it is not open */
  FileDescription get(int fd) { return fd >= 0 && fd < fds.size() ? fds.get(fd) : null; }

  /** The socket of a descriptor, or null after recording the error if the descriptor is not open or not a socket */
  Socket socket(int fd)
  {
    if (get(fd) instanceof Socket s)
      return s;
    fail(get(fd) == null ? "EBADF" : "ENOTSOCK");
    return null;
  }

  /** The result of a system call of this process as strace shows it: the length of the returned data, the value, or -1 with the error */
  String shown(Object result)
  {
    if (result == null || Integer.valueOf(-1).equals(result))
      return "-1 " + errno + " (" + STRERROR.getOrDefault(errno, errno) + ")";
    return result instanceof String data ? String.valueOf(data.length()) : String.valueOf(result);
  }

  /** The descriptions of the open descriptors above 2, sorted: the descriptor numbers do not matter */
  private List<String> openDescriptions()
  {
    List<String> res = new ArrayList<>();
    for (int fd = 3; fd < fds.size(); fd++)
      if (fds.get(fd) != null)
        res.add(fds.get(fd).describe());
    res.sort(null);
    return res;
  }

  @Override public boolean equals(Object o)
  {
    return o instanceof PosixProcess p && stdout.data.equals(p.stdout.data) && stderr.data.equals(p.stderr.data) &&
        openDescriptions().equals(p.openDescriptions());
  }

  @Override public int hashCode() { return stdout.data.hashCode() * 31 + stderr.data.hashCode(); }

  /** How other differs from this expected process, one line per difference */
  String diffTo(PosixProcess other)
  {
    StringBuilder sb = new StringBuilder();
    if (!stdout.data.equals(other.stdout.data))
      sb.append("The standard output contains '")
          .append(PosixWorld.visible(other.stdout.data))
          .append("' instead of '")
          .append(PosixWorld.visible(stdout.data))
          .append("'\n");
    if (!stderr.data.equals(other.stderr.data))
      sb.append("The standard error contains '")
          .append(PosixWorld.visible(other.stderr.data))
          .append("' instead of '")
          .append(PosixWorld.visible(stderr.data))
          .append("'\n");
    if (!openDescriptions().equals(other.openDescriptions()))
      sb.append("Open descriptors: ").append(other.openDescriptions()).append(" instead of ").append(openDescriptions()).append("\n");
    return sb.toString();
  }
}
