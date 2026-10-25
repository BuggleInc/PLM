package plm.universe.posix;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * A TCP socket: unconnected, listening (with its backlog of connections waiting for accept()) or connected to its remote socket.
 * A connected socket has a send queue, holding the data that the network did not transmit yet, and a receive queue, holding the
 * packets that arrived and were not received yet. The end of the stream reaches the remote socket once this one stopped writing
 * and its send queue is empty.
 */
final class Socket extends FileDescription {
  /** The address of the machine of the socket */
  final String ip;
  int port = -1;
  boolean listening;
  int backlogMax;
  /** The connections waiting for accept(), as the sockets that it will return */
  final Deque<Socket> backlog = new ArrayDeque<>();
  Socket remote;
  final StringBuilder outgoing = new StringBuilder();
  final Deque<String> incoming = new ArrayDeque<>();
  /** Everything that arrived on this socket, received or not, which the comparison of the worlds checks */
  final StringBuilder arrived = new StringBuilder();
  /** Whether this side stopped writing, with close() or shutdown() */
  boolean writeClosed;

  Socket(String ip) { this.ip = ip; }

  @Override Socket duplicate()
  {
    Socket res     = new Socket(ip);
    res.port       = port;
    res.listening  = listening;
    res.backlogMax = backlogMax;
    res.outgoing.append(outgoing);
    res.incoming.addAll(incoming);
    res.arrived.append(arrived);
    res.writeClosed = writeClosed;
    return res;
  }

  @Override void copyLinks(FileDescription original, Map<FileDescription, FileDescription> copies)
  {
    Socket other = (Socket)original;
    for (Socket s : other.backlog)
      backlog.add(s.copy(copies));
    remote = other.remote == null ? null : other.remote.copy(copies);
  }

  /** The amount of bytes that arrived and were not received yet */
  int available() { return incoming.stream().mapToInt(String::length).sum(); }

  /** Whether the end of the stream reached this socket: the remote one stopped writing, and all its data was transmitted */
  boolean eofArrived() { return remote != null && remote.writeClosed && remote.outgoing.isEmpty(); }

  /** Lets the network carry at most that many bytes of the send queue to the remote socket, in one packet, and returns them */
  String transmit(int max)
  {
    String packet = outgoing.substring(0, Math.min(max, outgoing.length()));
    outgoing.delete(0, packet.length());
    if (!packet.isEmpty()) {
      remote.incoming.add(packet);
      remote.arrived.append(packet);
    }
    return packet;
  }

  @Override String describe()
  {
    if (remote != null)
      return "socket connected to " + remote.ip + (remote.port >= 0 ? ":" + remote.port : "");
    return listening ? "socket listening on port " + port : "unconnected socket";
  }
}
