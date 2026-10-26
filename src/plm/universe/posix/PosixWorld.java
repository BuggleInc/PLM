package plm.universe.posix;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BooleanSupplier;
import javax.swing.ImageIcon;
import plm.core.lang.ProgrammingLanguage;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.EntityControlPanel;
import plm.universe.World;

/**
 * A POSIX microworld: the student's process and the processes of the scripted peers on the network (see PosixProcess and Peer),
 * and the kernel objects their descriptors point to (see FileDescription). The kernel executes
 * the system calls of all of them with the same code. Time only advances when the student calls a primitive: the peers then
 * execute their script as far as they can, so a scenario has a single causal history. A call that would block forever (both
 * sides waiting for each other) is reported as a deadlock instead of hanging.
 *
 * The world has a clock, counted in seconds. The system calls of the student take no time, but sleep() and the blocking calls
 * do: when nothing else can progress, the clock jumps to the next wake up of a sleeping peer. This keeps the causal history
 * unique while letting a scenario refuse an early connection, as a server that only accepts connections after a while.
 *
 * The constants and the error names are the Linux ones. A failing call returns -1 (or null for the calls returning data) and
 * sets the error name of the calling process, returned by lastError(). The system calls do not let the peers act: PosixEntity
 * does it around each call of the student, and the blocking calls let them act until the call can complete.
 */
public class PosixWorld extends World {
  public static final int AF_INET = 2, SOCK_STREAM = 1;
  public static final int O_RDONLY = 0, O_WRONLY = 1, O_RDWR = 2, O_CREAT = 0100, O_TRUNC = 01000, O_APPEND = 02000;
  public static final int SHUT_RD = 0, SHUT_WR = 1, SHUT_RDWR = 2;
  /** The address of the student's machine, to which the client peers connect */
  public static final String STUDENT_IP = "10.0.0.10";

  /** What the comparison of the worlds can check (see comparing()) */
  public enum Aspect {
    /** The standard output and error of the student's process */
    OUTPUT,
    /** The descriptors left open by the student's process, whatever their numbers */
    DESCRIPTORS,
    /** The regular files */
    FILES,
    /** The peers: the progress of their script, everything that arrived on their connections, and their open descriptors */
    PEERS
  }

  Map<String, String> files = new TreeMap<>();
  List<Peer> peers          = new ArrayList<>();
  /** The kernel side of the student's process */
  PosixProcess student = new PosixProcess(STUDENT_IP, false);
  /** The clock of the world, in seconds */
  int now;
  /** What happened so far, one event per line (a system call of the student's process, or a step of a peer), read by the UI */
  List<String> trace = new CopyOnWriteArrayList<>();
  /** The amount of refused connect() since the clock last advanced, to detect the busy loops */
  private int refusedInARow;
  /** What the comparison of the worlds checks */
  private Set<Aspect> compared = EnumSet.allOf(Aspect.class);

  public PosixWorld(String name)
  {
    super(name);
    addEntity(new PosixEntity());
  }

  /** Copy constructor, required by World.copy() (found by reflection). */
  public PosixWorld(PosixWorld world)
  {
    super(world.getName());
    reset(world);
  }

  public PosixWorld addFile(String path, String content)
  {
    files.put(path, content);
    return this;
  }

  /** Sets the parameters of the student's function in this world, which the entity reads (see World.getParameter()) */
  public PosixWorld setParameters(Object... parameters)
  {
    setParameter(parameters);
    return this;
  }

  /**
   * Only compares these aspects of the worlds, when the exercise does not check the rest: for example only the output and the
   * descriptors, when the student may talk with a server in several ways (such as choosing a nickname on IRC).
   */
  public PosixWorld comparing(Aspect... aspects)
  {
    compared = EnumSet.noneOf(Aspect.class);
    compared.addAll(List.of(aspects));
    return this;
  }

  /** Sets the data that the process finds on its standard input */
  public PosixWorld setStdin(String data)
  {
    student.stdin.data = data;
    return this;
  }

  /** Adds a peer, warning the author if its script leaves sent data that the network never transmits */
  public PosixWorld addPeer(Peer peer)
  {
    int pending = peer instanceof ScriptedPeer scripted ? scripted.untransmitted() : 0;
    if (pending > 0)
      System.err.println("Warning: in world " + getName() + ", the script of the " + peer.name() + " never transmits the last " + pending +
                         " bytes that it sends. Add some transmit() to its script.");
    peers.add(peer);
    return this;
  }

  @Override public void reset(World initialWorld)
  {
    PosixWorld other = (PosixWorld)initialWorld;
    files            = new TreeMap<>(other.files);
    peers            = new ArrayList<>();
    // Shared by the processes, whose descriptions may be shared or point to each other
    Map<FileDescription, FileDescription> copies = new IdentityHashMap<>();
    for (Peer p : other.peers)
      peers.add(p.copy(copies));
    student = new PosixProcess(other.student, copies);
    now      = other.now;
    trace    = new CopyOnWriteArrayList<>(other.trace);
    compared = EnumSet.copyOf(other.compared);
    super.reset(initialWorld);
  }

  @Override public ImageIcon getIcon() { return ResourcesCache.getIcon("img/world_buggle.png"); }

  @Override public WorldView getView() { return new PosixWorldView(this); }

  @Override public EntityControlPanel getEntityControlPanel() { return new PosixTracePanel(this); }

  /** Adds an event to the trace, stamped with the clock */
  void log(String who, String what) { trace.add("[t=" + now + "] " + who + ": " + what); }

  /** The data between double quotes and with its quotes escaped, as displayed by strace */
  static String quoted(String data) { return "\"" + visible(data.replace("\\", "\\\\").replace("\"", "\\\"")) + "\""; }

  /** The data written by a call in the buffer of the caller, marked as a side effect (or ... if the call failed and left it unchanged) */
  static String filled(String data) { return data == null ? "..." : "← " + quoted(data); }

  /** The data with its line breaks and tabulations escaped, to display it on a single line */
  static String visible(String data) { return data.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t"); }

  // ---------------------------------------------------------------- kernel internals

  /**
   * Lets every peer execute as many steps as possible, and returns whether any of them progressed. Each step is animated on its
   * own, so that the view shows what the peers and the network do, such as a packet arriving before the student receives it.
   */
  boolean advancePeers()
  {
    boolean progressed = false;
    for (Peer p : peers)
      while (p.advance(this)) {
        progressed = true;
        ((PosixEntity)getEntity(0)).animate();
      }
    return progressed;
  }

  /** The earliest end of the sleep of a peer, or -1 if no peer sleeps */
  private int nextWakeUp()
  {
    int res = -1;
    for (Peer p : peers)
      if (p.wakeUp() >= 0 && (res < 0 || p.wakeUp() < res))
        res = p.wakeUp();
    return res;
  }

  /** Moves the clock to the given time, letting the peers act at each wake up on the way */
  private void advanceClockTo(int time)
  {
    advancePeers();
    for (int wake = nextWakeUp(); wake >= 0 && wake <= time; wake = nextWakeUp()) {
      now = wake;
      advancePeers();
    }
    now           = Math.max(now, time);
    refusedInARow = 0;
    advancePeers();
  }

  /** Advances the peers (and the clock) until the condition holds, or reports a deadlock if nothing can progress anymore */
  private void waitUntil(BooleanSupplier ready, String call)
  {
    while (!ready.getAsBoolean()) {
      if (advancePeers())
        continue;
      int wake = nextWakeUp();
      if (wake < 0) {
        StringBuilder sb = new StringBuilder("Deadlock: your process waits forever in " + call + ", while the other processes are:");
        for (Peer p : peers)
          sb.append("\n  ").append(p.describe());
        throw new IllegalStateException(sb.toString());
      }
      advanceClockTo(wake);
    }
  }

  /** The processes of the world: the student's one, then the ones of the peers */
  List<PosixProcess> processes()
  {
    List<PosixProcess> res = new ArrayList<>(List.of(student));
    for (Peer p : peers)
      res.add(p.process);
    return res;
  }

  /** The socket listening on ip:port with room in its backlog, in any process, or null */
  private Socket listener(String ip, int port)
  {
    for (PosixProcess proc : processes())
      for (FileDescription o : proc.fds)
        if (o instanceof Socket s && s.listening && s.ip.equals(ip) && s.port == port && s.backlog.size() < s.backlogMax)
          return s;
    return null;
  }

  // ---------------------------------------------------------------- system calls

  int socket(PosixProcess proc, int domain, int socket_type, int protocol)
  {
    if (domain != AF_INET)
      return proc.fail("EAFNOSUPPORT");
    if (socket_type != SOCK_STREAM || protocol != 0)
      return proc.fail("EPROTONOSUPPORT");
    return proc.allocate(new Socket(proc.ip));
  }

  int bind(PosixProcess proc, int fd, String ip, int port)
  {
    Socket s = proc.socket(fd);
    if (s == null)
      return -1;
    if (s.port >= 0)
      return proc.fail("EINVAL");
    for (FileDescription o : proc.fds)
      if (o instanceof Socket other && other.port == port)
        return proc.fail("EADDRINUSE");
    s.port = port;
    return 0;
  }

  int listen(PosixProcess proc, int fd, int backlog)
  {
    Socket s = proc.socket(fd);
    if (s == null)
      return -1;
    if (s.port < 0 || s.remote != null) // Linux binds an unbound socket to a random port instead, which makes no sense here
      return proc.fail("EINVAL");
    s.listening  = true;
    s.backlogMax = Math.max(backlog, 1);
    return 0;
  }

  int accept(PosixProcess proc, int fd)
  {
    Socket s = proc.socket(fd);
    if (s == null)
      return -1;
    if (!s.listening)
      return proc.fail("EINVAL");
    waitUntil(() -> !s.backlog.isEmpty(), "accept(" + fd + ")");
    return proc.allocate(s.backlog.poll());
  }

  int connect(PosixProcess proc, int fd, String ip, int port)
  {
    Socket s = proc.socket(fd);
    if (s == null)
      return -1;
    if (s.remote != null || s.listening)
      return proc.fail("EISCONN");
    Socket server = listener(ip, port);
    if (server == null) {
      if (++refusedInARow > 1000)
        throw new IllegalStateException("Your process retries connect() in a busy loop: wait with sleep() before retrying.");
      return proc.fail("ECONNREFUSED");
    }
    Socket accepted = new Socket(server.ip); // the connection is established in the backlog, before accept() returns it
    accepted.port   = server.port;
    accepted.remote = s;
    s.remote        = accepted;
    server.backlog.add(accepted);
    return 0;
  }

  int send(PosixProcess proc, int fd, String data, int flags)
  {
    Socket s = proc.socket(fd);
    if (s == null)
      return -1;
    if (s.remote == null || s.writeClosed)
      return proc.fail("ENOTCONN");
    s.outgoing.append(data);
    if (!proc.scriptedNetwork)
      s.transmit(data.length());
    return data.length();
  }

  /** Returns at most len bytes of the packets that arrived, or the empty string at the end of the stream */
  String recv(PosixProcess proc, int fd, int len, int flags)
  {
    Socket s = proc.socket(fd);
    if (s == null)
      return null;
    if (s.remote == null) {
      proc.fail("ENOTCONN");
      return null;
    }
    waitUntil(() -> !s.incoming.isEmpty() || s.eofArrived(), "recv(" + fd + ")");
    StringBuilder res = new StringBuilder(); // everything that arrived, as much as the buffer holds
    while (!s.incoming.isEmpty() && res.length() < len) {
      String packet = s.incoming.poll();
      int taken     = Math.min(packet.length(), len - res.length());
      res.append(packet, 0, taken);
      if (taken < packet.length())
        s.incoming.addFirst(packet.substring(taken));
    }
    return res.toString();
  }

  int open(PosixProcess proc, String path, int flags)
  {
    int mode = flags & 3;
    if (!files.containsKey(path)) {
      if ((flags & O_CREAT) == 0)
        return proc.fail("ENOENT");
      files.put(path, "");
    }
    if ((flags & O_TRUNC) != 0 && mode != O_RDONLY)
      files.put(path, "");
    return proc.allocate(new RegularFile(path, mode != O_WRONLY, mode != O_RDONLY, (flags & O_APPEND) != 0));
  }

  String read(PosixProcess proc, int fd, int len)
  {
    FileDescription o = proc.get(fd);
    if (o instanceof Socket)
      return recv(proc, fd, len, 0);
    if (o instanceof StandardStream in && in.input) {
      int end    = Math.min(Math.max(len, 0), in.data.length());
      String res = in.data.substring(0, end);
      in.data    = in.data.substring(end);
      return res;
    }
    if (!(o instanceof RegularFile f) || !f.readable) {
      proc.fail("EBADF");
      return null;
    }
    String content = files.getOrDefault(f.path, ""); // an unlinked file reads as empty
    int end        = Math.min(content.length(), f.offset + Math.max(len, 0));
    String res     = f.offset < end ? content.substring(f.offset, end) : "";
    f.offset += res.length();
    return res;
  }

  int write(PosixProcess proc, int fd, String data)
  {
    FileDescription o = proc.get(fd);
    if (o instanceof Socket)
      return send(proc, fd, data, 0);
    if (o instanceof StandardStream out && !out.input) { // kept in the stream, whose data the view shows
      out.data += data;
      return data.length();
    }
    if (!(o instanceof RegularFile f) || !f.writable)
      return proc.fail("EBADF");
    if (!files.containsKey(f.path)) // an unlinked file, which nobody can read anymore
      return data.length();
    String content = files.get(f.path);
    if (f.append)
      f.offset = content.length();
    String after = f.offset + data.length() < content.length() ? content.substring(f.offset + data.length()) : "";
    files.put(f.path, content.substring(0, f.offset) + data + after);
    f.offset += data.length();
    return data.length();
  }

  /** Removes the file. Its open descriptors stay usable, but read it as empty and write in nothing (no inode is modelled). */
  int unlink(PosixProcess proc, String path) { return files.remove(path) == null ? proc.fail("ENOENT") : 0; }

  int close(PosixProcess proc, int fd)
  {
    FileDescription o = proc.get(fd);
    if (o == null)
      return proc.fail("EBADF");
    if (o instanceof Socket s)
      s.writeClosed = true;
    proc.fds.set(fd, null);
    return 0;
  }

  int shutdown(PosixProcess proc, int fd, int how)
  {
    Socket s = proc.socket(fd);
    if (s == null)
      return -1;
    if (how < SHUT_RD || how > SHUT_RDWR)
      return proc.fail("EINVAL");
    if (s.remote == null)
      return proc.fail("ENOTCONN");
    if (how != SHUT_RD)
      s.writeClosed = true;
    return 0;
  }

  int sleep(int seconds)
  {
    advanceClockTo(now + Math.max(seconds, 0));
    return 0;
  }

  // ---------------------------------------------------------------- comparison

  @Override public boolean equals(Object o) { return o instanceof PosixWorld w && student.equals(w.student) && files.equals(w.files) && peers.equals(w.peers); }

  @Override public int hashCode() { return (student.hashCode() * 31 + files.hashCode()) * 31 + peers.hashCode(); }

  /** Whether this world matches the expected one on the compared aspects (see comparing()), while equals() compares everything */
  @Override public boolean winning(World standard) { return standard.diffTo(this, null).isEmpty(); }

  /** How world differs from this expected one on the compared aspects, one line per difference */
  @Override public String diffTo(World world, ProgrammingLanguage lang)
  {
    PosixWorld other = (PosixWorld)world;
    StringBuilder sb = new StringBuilder(student.diffTo(other.student, compared.contains(Aspect.OUTPUT), compared.contains(Aspect.DESCRIPTORS)));
    for (String path : compared.contains(Aspect.FILES) ? files.keySet() : Set.<String>of())
      if (!files.get(path).equals(other.files.get(path)))
        sb.append("File '")
            .append(path)
            .append("' contains '")
            .append(visible(String.valueOf(other.files.get(path))))
            .append("' instead of '")
            .append(visible(files.get(path)))
            .append("'\n");
    for (String path : compared.contains(Aspect.FILES) ? other.files.keySet() : Set.<String>of())
      if (!files.containsKey(path))
        sb.append("File '").append(path).append("' should not exist\n");
    for (int i = 0; compared.contains(Aspect.PEERS) && i < peers.size(); i++)
      if (!peers.get(i).equals(other.peers.get(i)))
        sb.append("The peer is ").append(other.peers.get(i).describe()).append(" instead of ").append(peers.get(i).describe()).append("\n");
    return sb.toString();
  }
}
