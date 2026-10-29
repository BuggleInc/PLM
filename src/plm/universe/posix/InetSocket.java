package plm.universe.posix;

/** An AF_INET socket, of a stream (Socket) or of datagrams (DatagramSocket): the address of its machine, and its port once bound */
abstract class InetSocket extends FileDescription {
  /** The address of the machine of the socket */
  final String ip;
  int port = -1;
  /** Everything that arrived on this socket, received or not, which the comparison of the worlds checks */
  final StringBuilder arrived = new StringBuilder();

  InetSocket(String ip) { this.ip = ip; }

  /** Copies the state of other into this copy */
  void copyState(InetSocket other)
  {
    port = other.port;
    arrived.append(other.arrived);
  }
}
