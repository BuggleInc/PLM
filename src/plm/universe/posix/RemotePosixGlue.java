import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.IntUnaryOperator;

/*
 * The network API of the world, in place of the one of the JDK: Socket, ServerSocket and Thread.sleep() act on the world of the
 * exercise, and System.out and System.err write on its standard streams. The generated entity imports these classes by name (see
 * Glue.replaces()), so that the student's code uses them as the ones of java.net and java.lang.
 */

private static final int AF_INET = 2, SOCK_STREAM = 1, SHUT_RD = 0, SHUT_WR = 1;

static
{
  System.setOut(new PrintStream(new Descriptor(1), true, StandardCharsets.UTF_8));
  System.setErr(new PrintStream(new Descriptor(2), true, StandardCharsets.UTF_8));
}

/** The parameters of the world, for the code that calls the student's method */
public static String getParamString(int i)
{
  return plm_remote_getParamString(i);
}

public static int getParamInt(int i)
{
  return plm_remote_getParamInt(i);
}

/** The exception of the last failure in the world, as the JDK would throw it */
private static IOException failure()
{
  String error = plm_remote_lastError();
  return switch (error) {
    case "ECONNREFUSED" -> new ConnectException("Connection refused");
    case "EADDRINUSE" -> new BindException("Address already in use");
    default -> new SocketException(error);
  };
}

private static int checked(int result) throws IOException
{
  if (result < 0)
    throw failure();
  return result;
}

/** A descriptor of the world, as the stream under System.out and System.err */
private static final class Descriptor extends OutputStream {
  private final int fd;

  Descriptor(int fd) { this.fd = fd; }

  @Override public void write(int b) { write(new byte[] {(byte)b}, 0, 1); }

  @Override public void write(byte[] b, int off, int len) { plm_remote_write(fd, Arrays.copyOfRange(b, off, off + len)); }
}

/** What Socket and ServerSocket share: their descriptor in the world, once their socket is open */
private abstract static class SocketBase implements Closeable {
  int fd = -1;
  private boolean closed;

  /**
   * Opens the socket in the world, and prepares it with setup (connect, or bind and listen), which returns -1 when it fails. As in
   * the JDK, this socket is then closed.
   */
  void open(IntUnaryOperator setup, String alreadyOpen) throws IOException
  {
    if (closed)
      throw new SocketException("Socket is closed");
    if (fd >= 0)
      throw new SocketException(alreadyOpen);
    int socket = checked(plm_remote_socket(AF_INET, SOCK_STREAM, 0));
    if (setup.applyAsInt(socket) < 0) {
      IOException e = failure();
      plm_remote_close(socket);
      closed = true;
      throw e;
    }
    fd = socket;
  }

  /** The descriptor, if the socket is open */
  int descriptor(String notOpen) throws SocketException
  {
    if (closed)
      throw new SocketException("Socket is closed");
    if (fd < 0)
      throw new SocketException(notOpen);
    return fd;
  }

  public boolean isClosed() { return closed; }

  /** Closes the socket, once: as in the JDK, closing a closed socket does nothing */
  @Override public void close() throws IOException
  {
    if (closed)
      return;
    closed = true;
    if (fd >= 0)
      checked(plm_remote_close(fd));
  }
}

/** A TCP socket of the world, with the interface of java.net.Socket */
public static class Socket extends SocketBase {
  public Socket() {}

  public Socket(String host, int port) throws IOException { connect(new InetSocketAddress(host, port)); }

  private Socket(int fd) { this.fd = fd; }

  /** Connects to the endpoint, a resolved InetSocketAddress */
  public void connect(SocketAddress endpoint) throws IOException
  {
    InetSocketAddress address = (InetSocketAddress)endpoint;
    open(socket -> plm_remote_connect(socket, address.getHostString(), address.getPort()), "Already connected");
  }

  public boolean isConnected() { return fd >= 0; }

  private int connection() throws SocketException { return descriptor("Socket is not connected"); }

  /** The stream of the data received, whose read() returns -1 at the end of the stream. Closing it closes the socket. */
  public InputStream getInputStream() throws IOException
  {
    connection();
    return new InputStream() {
      @Override public int read() throws IOException
      {
        byte[] b = new byte[1];
        return read(b, 0, 1) < 0 ? -1 : b[0] & 0xff;
      }

      @Override public int read(byte[] b, int off, int len) throws IOException
      {
        if (len == 0)
          return 0;
        byte[] data = plm_remote_recv(connection(), len, 0);
        if (data == null)
          throw failure();
        if (data.length == 0)
          return -1;
        System.arraycopy(data, 0, b, off, data.length);
        return data.length;
      }

      @Override public void close() throws IOException
      {
        Socket.this.close();
      }
    };
  }

  /** The stream of the data to send, whose write() sends everything. Closing it closes the socket. */
  public OutputStream getOutputStream() throws IOException
  {
    connection();
    return new OutputStream() {
      @Override public void write(int b) throws IOException
      {
        write(new byte[] {(byte)b}, 0, 1);
      }

      @Override public void write(byte[] b, int off, int len) throws IOException
      {
        while (len > 0) {
          int sent = checked(plm_remote_send(connection(), Arrays.copyOfRange(b, off, off + len), 0));
          off += sent;
          len -= sent;
        }
      }

      @Override public void close() throws IOException
      {
        Socket.this.close();
      }
    };
  }

  public void shutdownInput() throws IOException { checked(plm_remote_shutdown(connection(), SHUT_RD)); }

  public void shutdownOutput() throws IOException { checked(plm_remote_shutdown(connection(), SHUT_WR)); }
}

/** A listening TCP socket of the world, with the interface of java.net.ServerSocket */
public static class ServerSocket extends SocketBase {
  public ServerSocket() {}

  public ServerSocket(int port) throws IOException { this(port, 50); }

  public ServerSocket(int port, int backlog) throws IOException { bind(new InetSocketAddress(port), backlog); }

  public void bind(SocketAddress endpoint) throws IOException { bind(endpoint, 50); }

  /** Binds to the endpoint, a resolved InetSocketAddress, and listens with that backlog (50 as in the JDK if it is not positive) */
  public void bind(SocketAddress endpoint, int backlog) throws IOException
  {
    InetSocketAddress address = (InetSocketAddress)endpoint;
    open(socket
         -> plm_remote_bind(socket, address.getHostString(), address.getPort()) < 0 ? -1 : plm_remote_listen(socket, backlog > 0 ? backlog : 50),
         "Already bound");
  }

  public boolean isBound() { return fd >= 0; }

  /** Waits for a connection, and returns its socket */
  public Socket accept() throws IOException { return new Socket(checked(plm_remote_accept(descriptor("Socket is not bound yet")))); }
}

/** The sleep() of java.lang.Thread, in the world, which counts whole seconds */
public static final class Thread {
  private Thread() {}

  public static void sleep(long millis) throws InterruptedException { plm_remote_sleep((int)((millis + 999) / 1000)); }
}
