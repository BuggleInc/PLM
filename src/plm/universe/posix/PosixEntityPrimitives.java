package plm.universe.posix;

import plm.core.lang.primitives.Primitive;
import plm.core.lang.primitives.PrimitiveGroups;
import plm.universe.EntityPrimitivesBase;

/**
 * The system calls of the POSIX world. Their signatures are the ones of the protocol between the student's code and the PLM:
 * the data travels as byte arrays, and the socket addresses as an IP and a port in host byte order. The glue of each language
 * wraps them into its own API (see RemotePosixGlue.*).
 */
@PrimitiveGroups({"Sockets", "Files", "Others"})
public interface PosixEntityPrimitives extends EntityPrimitivesBase {

  @Primitive(value = "Creates a socket and returns its descriptor. Only AF_INET and SOCK_STREAM are supported, with protocol 0.", group = "Sockets")
  int socket(int domain, int socket_type, int protocol);

  @Primitive(value = "Binds the socket to the given local port.", group = "Sockets") int bind(int fd, String ip, int port);

  @Primitive(value = "Marks the bound socket as accepting connections, with a queue of at most backlog pending ones.", group = "Sockets")
  int listen(int fd, int backlog);

  @Primitive(value = "Waits for a pending connection on the listening socket, and returns a new descriptor for it.", group = "Sockets") int accept(int fd);

  @Primitive(value = "Connects the socket to the given remote address.", group = "Sockets") int connect(int fd, String ip, int port);

  @Primitive(value = "Sends the data on the connected socket, and returns the amount of bytes sent.", group = "Sockets")
  int send(int fd, byte[] data, int flags);

  @Primitive(value = "Waits for data on the connected socket and returns at most len bytes of it, or nothing at the end of the stream.", group = "Sockets")
  byte[] recv(int fd, int len, int flags);

  @Primitive(value = "Closes one or both directions of the connected socket (SHUT_RD, SHUT_WR or SHUT_RDWR).", group = "Sockets") int shutdown(int fd, int how);

  @Primitive(value = "Opens the file with the given flags (O_RDONLY, O_WRONLY or O_RDWR, combined with O_CREAT, O_TRUNC or O_APPEND), "
                     + "and returns its descriptor.",
             group = "Files")
  int open(String path, int flags);

  @Primitive(value = "Reads at most len bytes from the descriptor.", group = "Files") byte[] read(int fd, int len);

  @Primitive(value = "Writes the data to the descriptor, and returns the amount of bytes written.", group = "Files") int write(int fd, byte[] data);

  @Primitive(value = "Closes the descriptor.", group = "Files") int close(int fd);

  @Primitive(value = "Removes the file.", group = "Files") int unlink(String path);

  @Primitive(value = "Waits for the given amount of seconds.", group = "Others") int sleep(int seconds);

  /** The name of the last error (e.g. "ECONNREFUSED"), used by the glue to set errno */
  @Primitive String lastError();
}
