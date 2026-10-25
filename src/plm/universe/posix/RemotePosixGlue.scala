import java.io.{Closeable, IOException, InputStream, OutputStream, PrintStream}
import java.net.{BindException, ConnectException, InetSocketAddress, SocketAddress, SocketException}
import java.nio.charset.StandardCharsets
import java.util.Arrays

/*
 * The network API of the world, in place of the one of the JDK, which Scala programs use: Socket, ServerSocket and Thread.sleep()
 * act on the world of the exercise, and System.out and System.err (thus println()) write on its standard streams. The generated
 * entity imports these classes by name (see Glue.replaces()), so that the student's code uses them as the ones of java.net and
 * java.lang. As in Scala, their accessors have no parentheses (socket.getInputStream).
 */

private val AF_INET     = 2
private val SOCK_STREAM = 1
private val SHUT_RD     = 0
private val SHUT_WR     = 1

// Before the first use of Console, which keeps the System.out of that time
System.setOut(new PrintStream(new Descriptor(1), true, StandardCharsets.UTF_8))
System.setErr(new PrintStream(new Descriptor(2), true, StandardCharsets.UTF_8))

/** The parameters of the world, for the code that calls the student's method */
def getParamString(i: Int): String = plm_remote_getParamString(i)

def getParamInt(i: Int): Int = plm_remote_getParamInt(i)

/** The exception of the last failure in the world, as the JDK would throw it */
private def failure(): IOException = plm_remote_lastError() match {
  case "ECONNREFUSED" => new ConnectException("Connection refused")
  case "EADDRINUSE"   => new BindException("Address already in use")
  case error          => new SocketException(error)
}

private def checked(result: Int): Int = if (result < 0) throw failure() else result

/** A descriptor of the world, as the stream under System.out and System.err */
private class Descriptor(fd: Int) extends OutputStream {
  override def write(b: Int): Unit = write(Array(b.toByte), 0, 1)

  override def write(b: Array[Byte], off: Int, len: Int): Unit = plm_remote_write(fd, Arrays.copyOfRange(b, off, off + len))
}

/** What Socket and ServerSocket share: their descriptor in the world, once their socket is open */
abstract class SocketBase extends Closeable {
  protected var fd    = -1
  private var closed  = false

  /**
   * Opens the socket in the world, and prepares it with setup (connect, or bind and listen), which returns -1 when it fails. As in
   * the JDK, this socket is then closed.
   */
  protected def open(setup: Int => Int, alreadyOpen: String): Unit = {
    if (closed)
      throw new SocketException("Socket is closed")
    if (fd >= 0)
      throw new SocketException(alreadyOpen)
    val socket = checked(plm_remote_socket(AF_INET, SOCK_STREAM, 0))
    if (setup(socket) < 0) {
      val e = failure()
      plm_remote_close(socket)
      closed = true
      throw e
    }
    fd = socket
  }

  /** The descriptor, if the socket is open */
  protected def descriptor(notOpen: String): Int = {
    if (closed)
      throw new SocketException("Socket is closed")
    if (fd < 0)
      throw new SocketException(notOpen)
    fd
  }

  def isClosed: Boolean = closed

  /** Closes the socket, once: as in the JDK, closing a closed socket does nothing */
  override def close(): Unit =
    if (!closed) {
      closed = true
      if (fd >= 0)
        checked(plm_remote_close(fd))
    }
}

/** A TCP socket of the world, with the interface of java.net.Socket */
class Socket private[RemotePosix] (connected: Int) extends SocketBase {
  fd = connected

  def this() = this(-1)

  def this(host: String, port: Int) = {
    this(-1)
    connect(new InetSocketAddress(host, port))
  }

  /** Connects to the endpoint, a resolved InetSocketAddress */
  def connect(endpoint: SocketAddress): Unit = {
    val address = endpoint.asInstanceOf[InetSocketAddress]
    open(socket => plm_remote_connect(socket, address.getHostString, address.getPort), "Already connected")
  }

  def isConnected: Boolean = fd >= 0

  private def connection: Int = descriptor("Socket is not connected")

  /** The stream of the data received, whose read() returns -1 at the end of the stream. Closing it closes the socket. */
  def getInputStream: InputStream = {
    connection
    new InputStream {
      override def read(): Int = {
        val b = new Array[Byte](1)
        if (read(b, 0, 1) < 0) -1 else b(0) & 0xff
      }

      override def read(b: Array[Byte], off: Int, len: Int): Int =
        if (len == 0) 0
        else {
          val data = plm_remote_recv(connection, len, 0)
          if (data == null)
            throw failure()
          System.arraycopy(data, 0, b, off, data.length)
          if (data.isEmpty) -1 else data.length
        }

      override def close(): Unit = Socket.this.close()
    }
  }

  /** The stream of the data to send, whose write() sends everything. Closing it closes the socket. */
  def getOutputStream: OutputStream = {
    connection
    new OutputStream {
      override def write(b: Int): Unit = write(Array(b.toByte), 0, 1)

      override def write(b: Array[Byte], off: Int, len: Int): Unit = {
        var sent = 0
        while (sent < len)
          sent += checked(plm_remote_send(connection, Arrays.copyOfRange(b, off + sent, off + len), 0))
      }

      override def close(): Unit = Socket.this.close()
    }
  }

  def shutdownInput(): Unit = checked(plm_remote_shutdown(connection, SHUT_RD))

  def shutdownOutput(): Unit = checked(plm_remote_shutdown(connection, SHUT_WR))
}

/** A listening TCP socket of the world, with the interface of java.net.ServerSocket */
class ServerSocket() extends SocketBase {
  def this(port: Int, backlog: Int) = {
    this()
    bind(new InetSocketAddress(port), backlog)
  }

  def this(port: Int) = this(port, 50)

  /** Binds to the endpoint, a resolved InetSocketAddress, and listens with that backlog (50 as in the JDK if it is not positive) */
  def bind(endpoint: SocketAddress, backlog: Int = 50): Unit = {
    val address = endpoint.asInstanceOf[InetSocketAddress]
    open(socket => if (plm_remote_bind(socket, address.getHostString, address.getPort) < 0) -1 else plm_remote_listen(socket, if (backlog > 0) backlog else 50),
         "Already bound")
  }

  def isBound: Boolean = fd >= 0

  /** Waits for a connection, and returns its socket */
  def accept(): Socket = new Socket(checked(plm_remote_accept(descriptor("Socket is not bound yet"))))
}

/** The sleep() of java.lang.Thread, in the world, which counts whole seconds */
object Thread {
  def sleep(millis: Long): Unit = plm_remote_sleep(((millis + 999) / 1000).toInt)
}
