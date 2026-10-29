package plm.universe.posix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A peer following a script (see Peer.server() and Peer.client()). Its script is a list of steps executed in order,
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
 * The steps take the descriptors of the peer's sockets, as the system calls: a server has its listening socket on 3, and gets its
 * connections from accept(3) on the lowest free descriptors (4, then 5...), while a client has an unconnected socket on 3 to
 * connect(), and can create more with socket(). The parsing follows the descriptors as the kernel allocates them, so that the
 * errors in a script (such as sending on a descriptor that is not a connection) are reported with their line when the exercise
 * is loaded.
 */
final class ScriptedPeer extends Peer {
  enum Kind { ACCEPT, SOCKET, CONNECT, SLEEP, SEND, TRANSMIT, RECV, CLOSE }
  /** A step of a script, on the descriptor fd (-1 for SLEEP) */
  record Step(Kind kind, int fd, int amount, String data) {}

  /** Whether the student connects to this peer, or this peer connects to the student */
  final boolean isServer;
  /** The port of a server peer, or the one of the student's machine to which a client peer connects */
  final int port;
  final List<Step> script = new ArrayList<>();
  int next;
  /** End of the current SLEEP step, or -1 when not sleeping */
  int wakeAt = -1;
  boolean refused;
  /** The last connection of each descriptor, kept after close() since the network may still carry its data */
  final Map<Integer, Socket> sockets = new TreeMap<>();

  /** While parsing: the open descriptors of the peer's process above 2, by kind of socket, and the connections closed since */
  private final Map<Integer, Kind> openAtParse = new TreeMap<>();
  private final TreeSet<Integer> closedAtParse = new TreeSet<>();

  /** A peer whose process has a socket on descriptor 3 from the start, bound to its port for a server */
  ScriptedPeer(boolean isServer, String ip, int port, String script)
  {
    super(ip);
    this.isServer = isServer;
    this.port     = port;
    Socket socket = new Socket(ip);
    if (isServer)
      socket.port = port;
    process.allocate(socket);
    openAtParse.put(3, isServer ? Kind.ACCEPT : Kind.SOCKET);
    parse(script);
  }

  /** Adds the steps of the script, reporting its errors with their line */
  private void parse(String text)
  {
    int pos = 0;
    while (true) {
      while (pos < text.length() && (Character.isWhitespace(text.charAt(pos)) || text.startsWith("//", pos)))
        if (text.startsWith("//", pos)) // a comment, until the end of the line
          pos = text.indexOf('\n', pos) < 0 ? text.length() : text.indexOf('\n', pos) + 1;
        else
          pos++;
      if (pos >= text.length())
        return;
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
            char escaped = text.charAt(pos); // \n, \r and \t, or the escaped char itself (such as \" or \\)
            sb.append(escaped == 'n' ? '\n' : escaped == 'r' ? '\r' : escaped == 't' ? '\t' : escaped);
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
      case "socket" -> 0;
      case "accept", "connect", "sleep", "close" -> 1;
      case "send", "transmit", "recv" -> 2;
      case "sendInPackets" -> 4;
      default -> throw new IllegalArgumentException("unknown call " + name + "()");
    };
    if (args.size() != expected)
      throw new IllegalArgumentException(name + "() takes " + expected + " arguments, not " + args.size());
    switch (name) {
      case "socket" -> socket();
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

  /** Waits for the student to connect, on the listening socket fd (of a server), and gets the connection on a new descriptor */
  private ScriptedPeer accept(int fd)
  {
    check(fd, Kind.ACCEPT, "the listening socket");
    opened(Kind.CONNECT);
    return add(Kind.ACCEPT, fd, 0, null);
  }
  /** Creates a new unconnected socket (of a client), on the lowest free descriptor */
  private ScriptedPeer socket()
  {
    if (isServer)
      throw new IllegalArgumentException("A server peer cannot create sockets");
    opened(Kind.SOCKET);
    return add(Kind.SOCKET, -1, 0, null);
  }
  /** Connects the unconnected socket fd (of a client) to the student's process, or is refused if it does not listen */
  private ScriptedPeer connect(int fd)
  {
    check(fd, Kind.SOCKET, "an unconnected socket");
    openAtParse.put(fd, Kind.CONNECT);
    return add(Kind.CONNECT, fd, 0, null);
  }
  private ScriptedPeer sleep(int seconds) { return add(Kind.SLEEP, -1, seconds, null); }
  /** Puts the data in the send queue of the connection fd, where it waits for the network (see transmit()) */
  private ScriptedPeer send(int fd, String data)
  {
    check(fd, Kind.CONNECT, "a connection");
    return add(Kind.SEND, fd, 0, data);
  }
  /** Lets the network carry at most that many bytes of the send queue of the connection fd to the student, in one packet */
  private ScriptedPeer transmit(int fd, int bytes)
  {
    if (!closedAtParse.contains(fd)) // the network still carries the data of a closed connection
      check(fd, Kind.CONNECT, "a connection");
    return add(Kind.TRANSMIT, fd, bytes, null);
  }
  /** Sends the data, and lets the network carry it from the connection fd in packets of that size, each after the given pause */
  private ScriptedPeer sendInPackets(int fd, String data, int packetSize, int pause)
  {
    send(fd, data);
    for (int sent = 0; sent < data.length(); sent += packetSize)
      sleep(pause).transmit(fd, packetSize);
    return this;
  }
  /** Waits until the student sent that many bytes on the connection fd (or closed the stream) */
  private ScriptedPeer recv(int fd, int length)
  {
    check(fd, Kind.CONNECT, "a connection");
    return add(Kind.RECV, fd, length, null);
  }
  private ScriptedPeer close(int fd)
  {
    if (!openAtParse.containsKey(fd))
      throw new IllegalArgumentException("Descriptor " + fd + " is not open at that step");
    if (openAtParse.remove(fd) == Kind.CONNECT)
      closedAtParse.add(fd);
    return add(Kind.CLOSE, fd, 0, null);
  }

  /** Checks that the descriptor is open at that step of the script, on that kind of socket */
  private void check(int fd, Kind kind, String what)
  {
    if (openAtParse.get(fd) != kind)
      throw new IllegalArgumentException("Descriptor " + fd + " is not " + what + " at that step");
  }

  /** Allocates the lowest free descriptor to a new socket of that kind, as the kernel will do */
  private void opened(Kind kind)
  {
    int fd = 3;
    while (openAtParse.containsKey(fd))
      fd++;
    openAtParse.put(fd, kind);
    closedAtParse.remove(fd);
  }

  private ScriptedPeer add(Kind kind, int fd, int amount, String data)
  {
    script.add(new Step(kind, fd, amount, data));
    return this;
  }

  private ScriptedPeer(ScriptedPeer other, Map<FileDescription, FileDescription> copies)
  {
    super(other, copies);
    isServer = other.isServer;
    port     = other.port;
    script.addAll(other.script);
    next    = other.next;
    wakeAt  = other.wakeAt;
    refused = other.refused;
    other.sockets.forEach((fd, socket) -> sockets.put(fd, socket.copy(copies)));
  }

  @Override protected ScriptedPeer copy(Map<FileDescription, FileDescription> copies) { return new ScriptedPeer(this, copies); }

  /** How many bytes the script sends without ever transmitting them, which the student would wait for forever */
  int untransmitted()
  {
    int pending = 0;
    for (Step step : script)
      if (step.kind() == Kind.SEND)
        pending += step.data().length();
      else if (step.kind() == Kind.TRANSMIT)
        pending = Math.max(0, pending - step.amount());
    return pending;
  }

  /** Executes the next step through the system calls of the world if it can complete, and returns whether it did */
  @Override boolean advance(PosixWorld world)
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
        connected(fd);
        event = call(step) + " = " + process.shown(fd);
      }
      case SOCKET -> event = call(step) + " = " + process.shown(world.socket(process, PosixWorld.AF_INET, PosixWorld.SOCK_STREAM, 0));
      case CONNECT -> {
        int result = world.connect(process, step.fd(), PosixWorld.STUDENT_IP, port);
        if (result < 0) {
          refused = true;
          world.log(name(), call(step) + " = " + process.shown(result));
          return false;
        }
        connected(step.fd());
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
      case TRANSMIT -> event = sockets.get(step.fd()).transmit(step.amount()).length() + " bytes";
      case RECV -> {
        Socket socket = sockets.get(step.fd());
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

  /** Records the connection established on that descriptor */
  private void connected(int fd)
  {
    Socket socket = (Socket)process.get(fd);
    connections.add(socket);
    sockets.put(fd, socket);
  }

  @Override int wakeUp() { return wakeAt; }

  @Override String name() { return isServer ? "server" : "client"; }

  @Override String title() { return process.ip + " (" + name() + ")"; }

  /** Who does that step in the trace: the network transmits (in a given direction), the peer does the rest */
  private String actor(Step step) { return step.kind() == Kind.TRANSMIT ? network() : name(); }

  /** The current step, which the peer waits in since it acted as far as it can (a sleep in progress knows its end) */
  @Override String ongoing()
  {
    if (refused || next == script.size())
      return null;
    return actor(script.get(next)) + ": " + call(script.get(next)) + (wakeAt >= 0 ? " until t=" + wakeAt : "");
  }

  @Override List<String> upcoming()
  {
    List<String> res = new ArrayList<>();
    for (int i = next + 1; !refused && i < script.size(); i++)
      res.add(actor(script.get(i)) + ": " + call(script.get(i)));
    return res;
  }

  /** A step of the script as a system call, in the style of strace but without its result */
  private String call(Step step)
  {
    return switch (step.kind()) {
      case ACCEPT -> "accept(" + step.fd() + ")";
      case SOCKET -> "socket(AF_INET, SOCK_STREAM, 0)";
      case CONNECT -> "connect(" + step.fd() + ", " + PosixWorld.STUDENT_IP + ":" + port + ")";
      case SLEEP -> "sleep(" + step.amount() + ")";
      case SEND -> "send(" + step.fd() + ", " + PosixWorld.excerpt(step.data()) + ", " + step.data().length() + ")";
      case TRANSMIT -> "up to " + step.amount() + " bytes";
      case RECV -> "recv(" + step.fd() + ", ..., " + step.amount() + ")";
      case CLOSE -> "close(" + step.fd() + ")";
    };
  }

  @Override String describe()
  {
    String state = refused ? " (connection refused)" : next < script.size() ? " (waiting in " + script.get(next).kind() + ")" : "";
    return (isServer ? "server " : "client of ") + process.ip + ":" + port + ", at step " + next + "/" + script.size() + state + ", received '" +
        PosixWorld.bytes(received()) + "'";
  }

  @Override public boolean equals(Object o)
  {
    return o instanceof ScriptedPeer p && isServer == p.isServer && process.ip.equals(p.process.ip) && port == p.port && script.equals(p.script) &&
        next == p.next && refused == p.refused && received().equals(p.received()) && process.equals(p.process);
  }

  @Override public int hashCode() { return Objects.hash(process.ip, port, next, received()); }
}
