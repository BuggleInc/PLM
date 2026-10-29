package plm.universe.posix;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A UDP socket, bound to its port by bind(), or on an ephemeral port by its first sendto() or connect(). Each datagram arrives
 * whole in the receive queue, with the address of its sender, and one call receives it whole: the bytes that do not fit in the
 * buffer are lost. connect() sets a default destination, and then only the datagrams coming from it arrive.
 */
final class DatagramSocket extends InetSocket {
  /** The default destination set by connect(), or null */
  String remoteIp;
  int remotePort;
  final Deque<Datagram> incoming = new ArrayDeque<>();

  DatagramSocket(String ip) { super(ip); }

  @Override DatagramSocket duplicate()
  {
    DatagramSocket res = new DatagramSocket(ip);
    res.copyState(this);
    res.remoteIp   = remoteIp;
    res.remotePort = remotePort;
    res.incoming.addAll(incoming);
    return res;
  }

  /** Whether a datagram from that address may arrive here, which connect() restricts */
  boolean accepts(String fromIp, int fromPort) { return remoteIp == null || remoteIp.equals(fromIp) && remotePort == fromPort; }

  /** Lets the datagram arrive in the receive queue */
  void deliver(Datagram datagram)
  {
    incoming.add(datagram);
    arrived.append(datagram.data());
  }

  @Override String describe() { return "datagram socket" + (remoteIp == null ? "" : " connected to " + remoteIp + ":" + remotePort); }
}
