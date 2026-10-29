package plm.universe.posix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A remote process, talking TCP or UDP with the student's process. It is a process of the world (see PosixProcess) whose system calls
 * come from the PLM instead of the student's code, on its own machine. A peer either follows a script (see Peer.server(),
 * Peer.client(), their UDP versions and ScriptedPeer), or is a Java program reacting to what it receives (see ProgramPeer).
 *
 * The world lets each peer act as far as it can before and after each system call of the student: advance() does one action of
 * the peer if it can, so that each of them is animated on its own.
 */
public abstract class Peer {
  /** The process of this peer, on its own machine, whose data waits in its send queues until the peer transmits it */
  final PosixProcess process;
  /** The connections and the datagram sockets of this peer, in the order they were opened, kept after close() */
  final List<InetSocket> endpoints = new ArrayList<>();
  /** When the peer started to wait in its current state, for the trace */
  int since;

  Peer(String ip) { process = new PosixProcess(ip, true); }

  /** Copies other, its sockets being copied in copies (see FileDescription.copy()) */
  Peer(Peer other, Map<FileDescription, FileDescription> copies)
  {
    process = new PosixProcess(other.process, copies);
    for (InetSocket socket : other.endpoints)
      endpoints.add(socket.copy(copies));
    since = other.since;
  }

  /** A peer on ip:port, to which the student connects (its script should accept() the connection) */
  public static Peer server(String ip, int port, String script) { return new ScriptedPeer(true, false, ip, port, script); }

  /**
   * A peer connecting to the port where the student's process listens (its script should connect()). The system calls of the
   * student taking no time, the script should sleep() before connecting to leave the student's process the time to listen.
   */
  public static Peer client(String ip, int port, String script) { return new ScriptedPeer(false, false, ip, port, script); }

  /** A peer on ip:port over UDP, to which the student sends datagrams (its script should recvfrom() them before answering) */
  public static Peer udpServer(String ip, int port, String script) { return new ScriptedPeer(true, true, ip, port, script); }

  /**
   * A peer sending datagrams over UDP to the port of the student's process. As for client(), the script should sleep() first:
   * the datagrams sent before the student's process binds its socket are lost.
   */
  public static Peer udpClient(String ip, int port, String script) { return new ScriptedPeer(false, true, ip, port, script); }

  /** A copy of this peer, its sockets being copied in copies (see FileDescription.copy()) */
  protected abstract Peer copy(Map<FileDescription, FileDescription> copies);

  /** Does the next action of this peer through the system calls of the world if it can, and returns whether it did */
  abstract boolean advance(PosixWorld world);

  /** When this peer wants to act on its own (at the end of a sleep), or -1 */
  abstract int wakeUp();

  /** The name of this peer in the trace, such as "server" */
  abstract String name();

  /** The title of its machine in the world view */
  abstract String title();

  /** What this peer waits for, as a line of the trace without its time, or null if it does not wait */
  abstract String ongoing();

  /** The next actions of this peer as lines of the trace, if they are known in advance */
  List<String> upcoming() { return List.of(); }

  /** The state of this peer, for the deadlocks and the differences between the worlds */
  abstract String describe();

  /** Who transmits the data of this peer in the trace */
  String network() { return "network (" + name() + " → you)"; }

  /** Everything that arrived on the connections and the datagram sockets of this peer, received by it or not */
  String received() { return endpoints.stream().map(socket -> socket.arrived.toString()).collect(Collectors.joining()); }
}
