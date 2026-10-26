package plm.universe.posix;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A peer written as a Java program, reacting to what it receives: a server of a real protocol (such as IRC or HTTP), serving
 * several clients and answering each request as it deserves. It is written as an event loop: start() prepares it (typically
 * listening on its ports), and each call to act() does at most one action through the helpers below, which trace their system
 * calls as the student's ones, and returns whether it did one. The world thus animates each action on its own. The state of the
 * program is in the fields of its class, which its copy constructor copies (after calling the one of ProgramPeer), and which
 * copy() calls.
 *
 * The data sent by the program reaches the student at once, unless it is sent in packets (see sendInPackets()), which the network
 * transmits one by one after a pause each, to make the framing of the messages matter.
 */
public abstract class ProgramPeer extends Peer {
  /** A packet waiting for the network: when it leaves, from which socket (even closed since), and how many bytes */
  private record Packet(int at, Socket socket, int bytes) {}

  private final String name;
  private boolean started;
  private final List<Packet> packets = new ArrayList<>();
  /** The world during advance(), which the helpers use */
  private PosixWorld world;

  protected ProgramPeer(String ip, String name)
  {
    super(ip);
    this.name = name;
  }

  /** Copies other, as the copy constructor of the subclass must do before copying its own fields */
  protected ProgramPeer(ProgramPeer other, Map<FileDescription, FileDescription> copies)
  {
    super(other, copies);
    name    = other.name;
    started = other.started;
    for (Packet packet : other.packets)
      packets.add(new Packet(packet.at(), packet.socket().copy(copies), packet.bytes()));
  }

  /** Prepares the program as it starts, typically by listening on its ports */
  protected abstract void start();

  /** Does at most one action of the program, and returns whether it did */
  protected abstract boolean act();

  /** What the program waits for, for the trace (such as "waiting for a command"), or null */
  protected String waiting() { return null; }

  /** When the program wants to act on its own, such as answering after a delay, or -1 */
  protected int timer() { return -1; }

  @Override final boolean advance(PosixWorld world)
  {
    this.world = world;
    try {
      boolean acted = true;
      if (started)
        acted = transmitPacket() || act();
      else
        start();
      started = true;
      if (acted)
        since = world.now;
      return acted;
    } finally {
      this.world = null;
    }
  }

  /** Lets the network transmit the next packet, if it is time */
  private boolean transmitPacket()
  {
    for (Packet packet : packets)
      if (packet.at() <= world.now) {
        packets.remove(packet);
        world.log(network(), packet.socket().transmit(packet.bytes()).length() + " bytes");
        return true;
      }
    return false;
  }

  @Override final int wakeUp()
  {
    int res = timer();
    for (Packet packet : packets)
      if (res < 0 || packet.at() < res)
        res = packet.at();
    return res;
  }

  @Override final String name() { return name; }

  @Override String title() { return process.ip + " (" + name + ")"; }

  @Override final String ongoing() { return waiting() == null ? null : name + ": " + waiting(); }

  @Override String describe()
  {
    return name + " " + process.ip + (waiting() == null ? "" : " (" + waiting() + ")") + ", received '" + PosixWorld.visible(received()) + "'";
  }

  // ---------------------------------------------------------------- helpers for act(), tracing their system calls

  /** The text in ASCII, which the world only accepts for now: the accents are removed (of a translated text), and the other characters become ? */
  protected static String ascii(String text)
  {
    String res = Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    return res.replaceAll("[^\\x00-\\x7F]", "?");
  }

  /** The time in the world */
  protected int now() { return world.now; }

  /** Traces a system call with its result, and returns the result */
  private <T> T traced(String call, T result)
  {
    world.log(name, call + " = " + process.shown(result));
    return result;
  }

  /** Listens on that port of all the interfaces, and returns the descriptor of the listening socket */
  protected int listen(int port)
  {
    int fd = traced("socket(AF_INET, SOCK_STREAM, 0)", world.socket(process, PosixWorld.AF_INET, PosixWorld.SOCK_STREAM, 0));
    traced("bind(" + fd + ", 0.0.0.0:" + port + ")", world.bind(process, fd, "0.0.0.0", port));
    traced("listen(" + fd + ", 16)", world.listen(process, fd, 16));
    return fd;
  }

  /** Accepts the next connection waiting on that listening socket, and returns its descriptor, or -1 if none waits */
  protected int accept(int fd)
  {
    if (((Socket)process.get(fd)).backlog.isEmpty())
      return -1;
    int res = traced("accept(" + fd + ")", world.accept(process, fd));
    connections.add((Socket)process.get(res));
    return res;
  }

  /** The data that arrived on that connection and was not received yet, without receiving it */
  protected String peek(int fd) { return String.join("", ((Socket)process.get(fd)).incoming); }

  /** Receives exactly that many bytes of that connection, which must have arrived (see peek()) */
  protected String receive(int fd, int length)
  {
    String data = world.recv(process, fd, length, 0);
    return traced("recv(" + fd + ", " + PosixWorld.filled(data) + ", " + length + ")", data);
  }

  /** Receives the data of that connection up to the delimiter, and returns it without the delimiter, or null if it did not arrive yet */
  protected String receive(int fd, String delimiter)
  {
    int end = peek(fd).indexOf(delimiter);
    return end < 0 ? null : receive(fd, end + delimiter.length()).substring(0, end);
  }

  /** Whether the other side closed that connection (the data that arrived before can still be received) */
  protected boolean ended(int fd) { return ((Socket)process.get(fd)).eofArrived(); }

  /** Sends the data on that connection, which the network transmits at once */
  protected void send(int fd, String data)
  {
    traced("send(" + fd + ", " + PosixWorld.quoted(data) + ", " + data.length() + ")", world.send(process, fd, data, 0));
    ((Socket)process.get(fd)).transmit(data.length());
  }

  /** Sends the data on that connection, which the network transmits in packets of that size, each after the given pause */
  protected void sendInPackets(int fd, String data, int packetSize, int pause)
  {
    traced("send(" + fd + ", " + PosixWorld.quoted(data) + ", " + data.length() + ")", world.send(process, fd, data, 0));
    for (int sent = 0, at = world.now + pause; sent < data.length(); sent += packetSize, at += pause)
      packets.add(new Packet(at, (Socket)process.get(fd), packetSize));
  }

  /** Closes that descriptor. The data sent before still reaches the student, in its packets. */
  protected void close(int fd) { traced("close(" + fd + ")", world.close(process, fd)); }

  @Override public boolean equals(Object o)
  {
    return o instanceof ProgramPeer p && p.getClass() == getClass() && process.ip.equals(p.process.ip) && received().equals(p.received()) &&
        process.equals(p.process);
  }

  @Override public int hashCode() { return process.ip.hashCode() * 31 + received().hashCode(); }
}
