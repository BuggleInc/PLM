package plm.universe.posix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * A scripted remote process, talking TCP with the student's process. It is a process of the world (see PosixProcess) whose system
 * calls come from its script instead of the student's code, on its own machine. Its script is a list of steps executed in order,
 * each of them either completing at once or waiting: ACCEPT waits for a connection, SLEEP waits until the clock of the world
 * reached its end, and RECV waits for enough data (or the end of the stream). CONNECT completes at once, but is refused if the
 * student's process does not listen on that port (or its backlog is full) at that time: the peer then stops there. The listening
 * socket of a server only listens once its script reaches its first ACCEPT, so that the connections are refused before.
 *
 * SEND only fills the send queue of the peer's socket: TRANSMIT moves the network, which carries some of these bytes to the
 * receive queue of the student in one packet. The scripts thus define how and when the data travels, which makes the short reads
 * visible. The end of the stream reaches the student once all the data sent before the CLOSE was transmitted. In the other
 * direction, what the student sends reaches the peer at once.
 *
 * The script is written with the syntax of the trace, one call per line, possibly commented with //:
 * <pre>
 * accept(3)
 * send(4, "Hello")
 * sleep(1)
 * transmit(4, 5)            // or sendInPackets(4, "Hello", 2, 1), sending in packets of 2 bytes, each after a pause of 1s
 * close(4)
 * </pre>
 * The steps take the descriptors of the peer's sockets, as the system calls: a server has its listening socket on 3 and accepts
 * its connection on 4, while a client has its connection on 3 (see connectionFd()). A peer only has one connection for now, so
 * the descriptors are only checked. The errors in a script are reported when the exercise is loaded.
 */
public class Peer {
  public enum Kind { ACCEPT, CONNECT, SLEEP, SEND, TRANSMIT, RECV, CLOSE }
  /** A step of a script, on the descriptor fd (-1 for SLEEP) */
  public record Step(Kind kind, int fd, int amount, String data) {}

  /** Whether the student connects to this peer, or this peer connects to the student */
  final boolean isServer;
  /** The port of a server peer, or the one of the student's machine to which a client peer connects */
  final int port;
  final List<Step> script = new ArrayList<>();
  int next;
  /** End of the current SLEEP step, or -1 when not sleeping */
  int wakeAt = -1;
  /** When the current step started, which the peers wait in as long as it cannot complete */
  int since;

  boolean refused;
  /** The process of this peer, on its machine, whose socket on descriptor 3 exists from the start, bound to its port for a server */
  final PosixProcess process;
  /** The connections of this peer by descriptor, kept after close() since the network may still carry their data */
  final Map<Integer, Socket> connections = new TreeMap<>();

  private Peer(boolean isServer, String ip, int port)
  {
    this.isServer = isServer;
    this.port     = port;
    process       = new PosixProcess(ip, true);
    Socket socket = new Socket(ip);
    if (isServer)
      socket.port = port;
    process.allocate(socket);
  }

  /** A peer on ip:port, to which the student connects (its script should accept() the connection) */
  public static Peer server(String ip, int port, String script) { return new Peer(true, ip, port).parse(script); }
  /**
   * A peer connecting to the port where the student's process listens (its script should connect()). The system calls of the
   * student taking no time, the script should sleep() before connecting to leave the student's process the time to listen.
   */
  public static Peer client(String ip, int port, String script) { return new Peer(false, ip, port).parse(script); }

  /** Adds the steps of the script, reporting its errors with their line */
  private Peer parse(String text)
  {
    int pos = 0;
    while (true) {
      while (pos < text.length() && (Character.isWhitespace(text.charAt(pos)) || text.startsWith("//", pos)))
        if (text.startsWith("//", pos)) // a comment, until the end of the line
          pos = text.indexOf('\n', pos) < 0 ? text.length() : text.indexOf('\n', pos) + 1;
        else
          pos++;
      if (pos >= text.length())
        return this;
      int line = (int)text.substring(0, pos).chars().filter(c -> c == '\n').count() + 1;
      try {
        int open = text.indexOf('(', pos);
        if (open < 0)
          throw new IllegalArgumentException("a call such as sleep(1) was expected");
        String name       = text.substring(pos, open).strip();
        List<Object> args = new ArrayList<>();
        pos               = parseArguments(text, open + 1, args);
        call(name, args);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Line " + line + " of the script of the " + name() + " peer: " + e.getMessage(), e);
      } catch (IndexOutOfBoundsException e) {
        throw new IllegalArgumentException("Line " + line + " of the script of the " + name() + " peer: unterminated call or string", e);
      } catch (ClassCastException e) {
        throw new IllegalArgumentException("Line " + line + " of the script of the " + name() + " peer: an argument has the wrong type", e);
      }
    }
  }

  /** Reads the arguments (integers and quoted strings) of a call until its closing parenthesis, and returns the position after it */
  private static int parseArguments(String text, int pos, List<Object> args)
  {
    while (true) {
      while (Character.isWhitespace(text.charAt(pos)))
        pos++;
      char c = text.charAt(pos);
      if (c == ')')
        return pos + 1;
      if (c == ',') {
        pos++;
        continue;
      }
      if (c == '"') {
        StringBuilder sb = new StringBuilder();
        for (pos++; text.charAt(pos) != '"'; pos++) {
          if (text.charAt(pos) == '\\') {
            pos++;
            sb.append(text.charAt(pos) == 'n' ? '\n' : text.charAt(pos));
          } else
            sb.append(text.charAt(pos));
        }
        args.add(sb.toString());
        pos++;
      } else {
        int start = pos;
        while (Character.isDigit(text.charAt(pos)))
          pos++;
        if (start == pos)
          throw new IllegalArgumentException("unexpected '" + c + "'");
        args.add(Integer.parseInt(text.substring(start, pos)));
      }
    }
  }

  /** Adds the steps of a call of the script */
  private void call(String name, List<Object> args)
  {
    int expected = switch (name) {
      case "accept", "connect", "sleep", "close" -> 1;
      case "send", "transmit", "recv" -> 2;
      case "sendInPackets" -> 4;
      default -> throw new IllegalArgumentException("unknown call " + name + "()");
    };
    if (args.size() != expected)
      throw new IllegalArgumentException(name + "() takes " + expected + " arguments, not " + args.size());
    switch (name) {
      case "accept" -> accept((Integer)args.get(0));
      case "connect" -> connect((Integer)args.get(0));
      case "sleep" -> sleep((Integer)args.get(0));
      case "close" -> close((Integer)args.get(0));
      case "send" -> send((Integer)args.get(0), (String)args.get(1));
      case "transmit" -> transmit((Integer)args.get(0), (Integer)args.get(1));
      case "recv" -> recv((Integer)args.get(0), (Integer)args.get(1));
      default -> sendInPackets((Integer)args.get(0), (String)args.get(1), (Integer)args.get(2), (Integer)args.get(3));
    }
  }

  /** Waits for the student to connect, on the listening socket fd (of a server) */
  private Peer accept(int fd)
  {
    checkFd(fd, isServer ? 3 : -1);
    return add(Kind.ACCEPT, fd, 0, null);
  }
  /** Connects the socket fd (of a client) to the student's process, or is refused if it does not listen */
  private Peer connect(int fd)
  {
    checkFd(fd, isServer ? -1 : 3);
    return add(Kind.CONNECT, fd, 0, null);
  }
  private Peer sleep(int seconds) { return add(Kind.SLEEP, -1, seconds, null); }
  /** Puts the data in the send queue of the connection fd, where it waits for the network (see transmit()) */
  private Peer send(int fd, String data)
  {
    checkFd(fd, connectionFd());
    return add(Kind.SEND, fd, 0, data);
  }
  /** Lets the network carry at most that many bytes of the send queue of the connection fd to the student, in one packet */
  private Peer transmit(int fd, int bytes)
  {
    checkFd(fd, connectionFd());
    return add(Kind.TRANSMIT, fd, bytes, null);
  }
  /** Sends the data, and lets the network carry it from the connection fd in packets of that size, each after the given pause */
  private Peer sendInPackets(int fd, String data, int packetSize, int pause)
  {
    send(fd, data);
    for (int sent = 0; sent < data.length(); sent += packetSize)
      sleep(pause).transmit(fd, packetSize);
    return this;
  }
  /** Waits until the student sent that many bytes on the connection fd (or closed the stream) */
  private Peer recv(int fd, int length)
  {
    checkFd(fd, connectionFd());
    return add(Kind.RECV, fd, length, null);
  }
  private Peer close(int fd)
  {
    checkFd(fd, connectionFd());
    return add(Kind.CLOSE, fd, 0, null);
  }

  /** Checks that a step uses the expected descriptor (-1 if that kind of peer cannot do that step) */
  private void checkFd(int fd, int expected)
  {
    if (expected < 0)
      throw new IllegalArgumentException("A " + name() + " peer cannot do that");
    if (fd != expected)
      throw new IllegalArgumentException("This step of a " + name() + " peer is on descriptor " + expected + ", not " + fd);
  }

  private Peer add(Kind kind, int fd, int amount, String data)
  {
    script.add(new Step(kind, fd, amount, data));
    return this;
  }

  /** Copies other, its sockets being copied in copies (see FileDescription.copy()) */
  Peer(Peer other, Map<FileDescription, FileDescription> copies)
  {
    isServer = other.isServer;
    port     = other.port;
    script.addAll(other.script);
    next    = other.next;
    wakeAt  = other.wakeAt;
    since   = other.since;
    refused = other.refused;
    process = new PosixProcess(other.process, copies);
    other.connections.forEach((fd, socket) -> connections.put(fd, socket.copy(copies)));
  }

  /** Executes the next step through the system calls of the world if it can complete, and returns whether it did */
  boolean advance(PosixWorld world)
  {
    if (next == script.size() || refused)
      return false;
    Step step = script.get(next);
    String event;
    switch (step.kind()) {
      case ACCEPT -> {
        Socket listening = (Socket)process.get(step.fd());
        if (!listening.listening)
          world.listen(process, step.fd(), 1);
        if (listening.backlog.isEmpty())
          return false;
        int fd = world.accept(process, step.fd());
        connections.put(fd, (Socket)process.get(fd));
        event = call(step) + " = " + process.shown(fd);
      }
      case CONNECT -> {
        int result = world.connect(process, step.fd(), PosixWorld.STUDENT_IP, port);
        if (result < 0) {
          refused = true;
          world.log(name(), call(step) + " = " + process.shown(result));
          return false;
        }
        connections.put(step.fd(), (Socket)process.get(step.fd()));
        event = call(step) + " = " + process.shown(result);
      }
      case SLEEP -> {
        if (wakeAt < 0)
          wakeAt = world.now + step.amount();
        if (world.now < wakeAt)
          return false;
        wakeAt = -1;
        event  = call(step);
      }
      case SEND -> {
        world.send(process, step.fd(), step.data(), 0);
        event = call(step);
      }
      case TRANSMIT -> event = connections.get(step.fd()).transmit(step.amount()).length() + " bytes";
      case RECV -> {
        Socket socket = connections.get(step.fd());
        if (socket.available() < step.amount() && !socket.eofArrived())
          return false;
        String data = world.recv(process, step.fd(), step.amount(), 0);
        event       = "recv(" + step.fd() + ", " + PosixWorld.filled(data) + ", " + step.amount() + ") = " + process.shown(data);
      }
      default -> { // CLOSE
        event = call(step) + " = " + process.shown(world.close(process, step.fd()));
      }
    }
    world.log(actor(step), event);
    next++;
    since = world.now;
    return true;
  }

  /** The name of this peer in the trace */
  String name() { return isServer ? "server" : "client"; }

  /** Everything that arrived on the connections of this peer, received by its script or not */
  String received() { return connections.values().stream().map(socket -> socket.arrived.toString()).collect(Collectors.joining()); }

  /** Who does that step in the trace: the network transmits (in a given direction), the peer does the rest */
  String actor(Step step) { return step.kind() == Kind.TRANSMIT ? "network (" + name() + " → you)" : name(); }

  /**
   * The descriptor of the connection in the peer's process: after 0, 1 and 2, a server has its listening socket (3) and then the
   * accepted one (4), while a client has its connected socket (3).
   */
  int connectionFd() { return isServer ? 4 : 3; }

  /** A step of the script as a system call, in the style of strace but without its result */
  String call(Step step)
  {
    return switch (step.kind()) {
      case ACCEPT -> "accept(" + step.fd() + ")";
      case CONNECT -> "connect(" + step.fd() + ", " + PosixWorld.STUDENT_IP + ":" + port + ")";
      case SLEEP -> "sleep(" + step.amount() + ")";
      case SEND -> "send(" + step.fd() + ", " + PosixWorld.quoted(step.data()) + ", " + step.data().length() + ")";
      case TRANSMIT -> "up to " + step.amount() + " bytes";
      case RECV -> "recv(" + step.fd() + ", ..., " + step.amount() + ")";
      case CLOSE -> "close(" + step.fd() + ")";
    };
  }

  String describe()
  {
    String state = refused ? " (connection refused)" : next < script.size() ? " (waiting in " + script.get(next).kind() + ")" : "";
    return (isServer ? "server " : "client of ") + process.ip + ":" + port + ", at step " + next + "/" + script.size() + state + ", received '" +
        PosixWorld.visible(received()) + "'";
  }

  @Override public boolean equals(Object o)
  {
    return o instanceof Peer p && isServer == p.isServer && process.ip.equals(p.process.ip) && port == p.port && script.equals(p.script) && next == p.next &&
        refused == p.refused && received().equals(p.received()) && process.equals(p.process);
  }

  @Override public int hashCode() { return Objects.hash(process.ip, port, next, received()); }
}
