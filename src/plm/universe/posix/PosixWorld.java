package plm.universe.posix;

import java.nio.charset.StandardCharsets;
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
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.core.ui.WorldView;
import plm.universe.EntityControlPanel;
import plm.universe.ExplainedException;
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
  public static final int AF_INET = 2, SOCK_STREAM = 1, SOCK_DGRAM = 2;
  /** The largest datagram that UDP carries over IPv4 */
  public static final int MAX_DATAGRAM = 65507;
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
  /** The states of the world, the initial one and then one after each event of the trace (at most MAX_SCENES), read by the view */
  List<Scene> history         = new CopyOnWriteArrayList<>();
  static final int MAX_SCENES = 1000;
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
    history  = new CopyOnWriteArrayList<>(other.history); // the scenes are never modified, so they can be shared
    compared = EnumSet.copyOf(other.compared);
    super.reset(initialWorld);
    if (history.isEmpty())
      history.add(Scene.after(this, null, null, -1));
  }

  @Override public ImageIcon getIcon() { return ResourcesCache.getIcon("img/world_buggle.png"); }

  @Override public WorldView getView() { return new PosixWorldView(this); }

  @Override public EntityControlPanel getEntityControlPanel() { return new PosixTracePanel(this); }

  /** Expands [!protocols/] into the documentation of the protocol of each class of program peer (once per class), under a heading if any */
  @Override protected String expandPlaceholders(String html)
  {
    StringBuilder protocols = new StringBuilder();
    peers.stream().filter(ProgramPeer.class ::isInstance).map(Object::getClass).distinct().forEach(server -> protocols.append(documentation(server)));
    if (!protocols.isEmpty())
      protocols.insert(0, "<h2>" + Game.i18n.tr("The servers of this world") + "</h2>\n");
    return super.expandPlaceholders(html).replace("[!protocols/]", protocols);
  }

  /** Adds an event of that process (null for the network) to the trace, stamped with the clock, and keeps the state it leads to */
  void log(PosixProcess actor, String who, String what)
  {
    trace.add("[t=" + now + "] " + who + ": " + what);
    if (history.size() < MAX_SCENES)
      history.add(Scene.after(this, who, what, processes().indexOf(actor)));
  }

  /** The data longer than this is cut in the trace, as strace does (see its option -s) */
  static final int SHOWN_DATA = 50;

  /** The bytes between double quotes and with their quotes escaped, as displayed by strace (whole, as for a path) */
  static String quoted(String data) { return "\"" + escaped(data.replace("\\", "\\\\").replace("\"", "\\\""), true) + "\""; }

  /** The data of a call as displayed by strace: quoted, and cut after SHOWN_DATA characters with ... after the quotes */
  static String excerpt(String data) { return data.length() <= SHOWN_DATA ? quoted(data) : quoted(data.substring(0, SHOWN_DATA)) + "..."; }

  /** The data written by a call in the buffer of the caller, marked as a side effect (or ... if the call failed and left it unchanged) */
  static String filled(String data) { return data == null ? "..." : "← " + excerpt(data); }

  /** The text written by a program, decoded from UTF-8 to display it */
  static String text(String data) { return new String(data.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8); }

  /** The text written by a program, decoded and on a single line, with its line breaks and other control characters escaped */
  static String visible(String data) { return escaped(text(data), false); }

  /** The bytes on a single line, as displayed by strace: the line breaks and tabulations escaped, and the other unprintable bytes in octal */
  static String bytes(String data) { return escaped(data, true); }

  /** The characters with \\n, \\r and \\t escaped, and the other control characters (and the non-ASCII ones if asked) in octal */
  private static String escaped(String data, boolean nonAscii)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < data.length(); i++) {
      char c = data.charAt(i);
      if (c == '\n' || c == '\r' || c == '\t')
        sb.append(c == '\n' ? "\\n" : c == '\r' ? "\\r" : "\\t");
      else if (c >= ' ' && c != 0x7F && (c < 0x7F || !nonAscii))
        sb.append(c);
      else { // on three digits when an octal digit follows, as strace does, to not read it as part of the number
        boolean digit = i + 1 < data.length() && data.charAt(i + 1) >= '0' && data.charAt(i + 1) <= '7';
        sb.append('\\').append(digit ? String.format("%03o", (int)c) : Integer.toOctalString(c));
      }
    }
    return sb.toString();
  }

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
        StringBuilder sb = new StringBuilder(Game.i18n.tr("Deadlock: your process waits forever in {0}, while the other processes are:", call));
        for (Peer p : peers)
          sb.append("\n  ").append(p.describe());
        throw new ExplainedException(sb.toString());
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

  /** The datagram socket bound to ip:port in any process, or null */
  private DatagramSocket datagramSocket(String ip, int port)
  {
    for (PosixProcess proc : processes())
      for (FileDescription o : proc.fds)
        if (o instanceof DatagramSocket s && s.ip.equals(ip) && s.port == port)
          return s;
    return null;
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
    if (socket_type != SOCK_STREAM && socket_type != SOCK_DGRAM || protocol != 0)
      return proc.fail("EPROTONOSUPPORT");
    return proc.allocate(socket_type == SOCK_STREAM ? new Socket(proc.ip) : new DatagramSocket(proc.ip));
  }

  /** Binds the socket to the port, which a socket of the other kind (TCP or UDP) may use too */
  int bind(PosixProcess proc, int fd, String ip, int port)
  {
    InetSocket s = proc.socket(fd, InetSocket.class);
    if (s == null)
      return -1;
    if (s.port >= 0)
      return proc.fail("EINVAL");
    if (proc.bound(s.getClass(), port))
      return proc.fail("EADDRINUSE");
    s.port = port;
    return 0;
  }

  int listen(PosixProcess proc, int fd, int backlog)
  {
    Socket s = proc.socket(fd, Socket.class);
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
    Socket s = proc.socket(fd, Socket.class);
    if (s == null)
      return -1;
    if (!s.listening)
      return proc.fail("EINVAL");
    waitUntil(() -> !s.backlog.isEmpty(), "accept(" + fd + ")");
    return proc.allocate(s.backlog.poll());
  }

  /** Connects a stream socket to the listening one, or sets the default destination of a datagram socket */
  int connect(PosixProcess proc, int fd, String ip, int port)
  {
    if (proc.get(fd) instanceof DatagramSocket d) {
      if (d.port < 0)
        d.port = proc.ephemeralPort();
      d.remoteIp   = ip;
      d.remotePort = port;
      return 0;
    }
    Socket s = proc.socket(fd, Socket.class);
    if (s == null)
      return -1;
    if (s.remote != null || s.listening)
      return proc.fail("EISCONN");
    Socket server = listener(ip, port);
    if (server == null) {
      if (++refusedInARow > 1000)
        throw new ExplainedException(Game.i18n.tr("Your process retries connect() in a busy loop: wait with sleep() before retrying."));
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
    if (proc.get(fd) instanceof DatagramSocket d)
      return d.remoteIp == null ? proc.fail("EDESTADDRREQ") : sendto(proc, fd, data, flags, d.remoteIp, d.remotePort);
    Socket s = proc.socket(fd, Socket.class);
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
    if (proc.get(fd) instanceof DatagramSocket) {
      Datagram datagram = recvfrom(proc, fd, len, flags);
      return datagram == null ? null : datagram.data();
    }
    Socket s = proc.socket(fd, Socket.class);
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

  /**
   * Sends a datagram to ip:port, which arrives at once if a socket is bound there, and is lost otherwise. On a stream socket, the
   * address is ignored, as Linux does.
   */
  int sendto(PosixProcess proc, int fd, String data, int flags, String ip, int port)
  {
    if (proc.get(fd) instanceof Socket)
      return send(proc, fd, data, flags);
    DatagramSocket s = proc.socket(fd, DatagramSocket.class);
    if (s == null)
      return -1;
    if (data.length() > MAX_DATAGRAM)
      return proc.fail("EMSGSIZE");
    if (s.port < 0)
      s.port = proc.ephemeralPort();
    DatagramSocket target = datagramSocket(ip, port);
    if (target != null && target.accepts(proc.ip, s.port))
      target.deliver(new Datagram(proc.ip, s.port, data));
    else
      log(null, "network", "datagram from " + proc.ip + ":" + s.port + " lost, as no socket waits for it on " + ip + ":" + port);
    return data.length();
  }

  /**
   * Waits for the next datagram and returns it, cut to len bytes (the rest of it is lost). On a stream socket, returns what recv()
   * does, with the address of the remote socket.
   */
  Datagram recvfrom(PosixProcess proc, int fd, int len, int flags)
  {
    if (proc.get(fd) instanceof Socket stream) {
      String data = recv(proc, fd, len, flags);
      return data == null ? null : new Datagram(stream.remote.ip, stream.remote.port, data);
    }
    DatagramSocket s = proc.socket(fd, DatagramSocket.class);
    if (s == null)
      return null;
    waitUntil(() -> !s.incoming.isEmpty(), "recvfrom(" + fd + ")");
    Datagram datagram = s.incoming.poll();
    int kept          = Math.min(datagram.data().length(), Math.max(len, 0));
    return new Datagram(datagram.ip(), datagram.port(), datagram.data().substring(0, kept));
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
    if (o instanceof InetSocket)
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
    if (o instanceof InetSocket)
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
    if (proc.get(fd) instanceof DatagramSocket d) // nothing to close on a connected datagram socket
      return d.remoteIp == null ? proc.fail("ENOTCONN") : 0;
    Socket s = proc.socket(fd, Socket.class);
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

  /**
   * Compares the names too, as World.equals() does: the worlds of an exercise often start alike (their peers differ by their
   * scenario, which is not compared), and the combo box selecting a world picks the first one equal to the chosen one
   */
  @Override public boolean equals(Object o)
  {
    return o instanceof PosixWorld w && getName().equals(w.getName()) && student.equals(w.student) && files.equals(w.files) && peers.equals(w.peers);
  }

  @Override public int hashCode() { return ((getName().hashCode() * 31 + student.hashCode()) * 31 + files.hashCode()) * 31 + peers.hashCode(); }

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
