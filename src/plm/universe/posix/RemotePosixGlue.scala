import java.io.{Closeable, FileNotFoundException, IOException, InputStream, InputStreamReader, OutputStream, OutputStreamWriter, PrintStream, Writer}
import java.net.{BindException, ConnectException, InetSocketAddress, SocketAddress, SocketException}
import java.nio.charset.StandardCharsets
import java.nio.file.{NoSuchFileException, OpenOption, Path, StandardOpenOption}
import java.util.Arrays

/*
 * The network and file API of the world, in place of the one of the JDK, which Scala programs use: Socket, ServerSocket,
 * Thread.sleep(), the file streams (FileInputStream, FileOutputStream, FileReader, FileWriter, PrintWriter) and the usual methods
 * of Files act on the world of the exercise, and System.out and System.err (thus println()) write on its standard streams. The
 * generated entity imports these classes by name (see Glue.replaces()), so that the student's code uses them as the ones of the
 * JDK. As in Scala, their accessors have no parentheses (socket.getInputStream). scala.io.Source.fromFile() is not replaced.
 */

private val AF_INET     = 2
private val SOCK_STREAM = 1
private val SHUT_RD     = 0
private val SHUT_WR     = 1
private val O_RDONLY    = 0
private val O_WRONLY    = 1
private val O_CREAT     = 0x40
private val O_TRUNC     = 0x200
private val O_APPEND    = 0x400

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

/** Opens a file of the world, and returns its descriptor, or throws the FileNotFoundException of the JDK */
private def opened(path: String, flags: Int): Int = {
  val fd = plm_remote_open(path, flags)
  if (fd < 0) {
    val error = plm_remote_lastError()
    throw new FileNotFoundException(path + " (" + (if (error == "ENOENT") "No such file or directory" else error) + ")")
  }
  fd
}

/** A file of the world opened for reading, with the interface of java.io.FileInputStream */
class FileInputStream(name: String) extends InputStream {
  private val fd     = opened(name, O_RDONLY)
  private var closed = false

  override def read(): Int = {
    val b = new Array[Byte](1)
    if (read(b, 0, 1) < 0) -1 else b(0) & 0xff
  }

  override def read(b: Array[Byte], off: Int, len: Int): Int =
    if (len == 0) 0
    else {
      val data = plm_remote_read(fd, len)
      if (data == null)
        throw failure()
      System.arraycopy(data, 0, b, off, data.length)
      if (data.isEmpty) -1 else data.length
    }

  override def close(): Unit =
    if (!closed) {
      closed = true
      checked(plm_remote_close(fd))
    }
}

/** A file of the world opened for writing, with the interface of java.io.FileOutputStream */
class FileOutputStream(name: String, append: Boolean) extends OutputStream {
  private val fd     = opened(name, O_WRONLY | O_CREAT | (if (append) O_APPEND else O_TRUNC))
  private var closed = false

  def this(name: String) = this(name, false)

  override def write(b: Int): Unit = write(Array(b.toByte), 0, 1)

  override def write(b: Array[Byte], off: Int, len: Int): Unit = {
    var written = 0
    while (written < len)
      written += checked(plm_remote_write(fd, Arrays.copyOfRange(b, off + written, off + len)))
  }

  override def close(): Unit =
    if (!closed) {
      closed = true
      checked(plm_remote_close(fd))
    }
}

/** A text file of the world opened for reading, with the interface of java.io.FileReader (in UTF-8) */
class FileReader(name: String) extends InputStreamReader(new FileInputStream(name), StandardCharsets.UTF_8)

/** A text file of the world opened for writing, with the interface of java.io.FileWriter (in UTF-8) */
class FileWriter(name: String, append: Boolean) extends OutputStreamWriter(new FileOutputStream(name, append), StandardCharsets.UTF_8) {
  def this(name: String) = this(name, false)
}

/** The java.io.PrintWriter of the JDK, whose constructor taking a file name opens it in the world */
class PrintWriter(out: Writer, autoFlush: Boolean) extends java.io.PrintWriter(out, autoFlush) {
  def this(out: Writer) = this(out, false)

  def this(out: OutputStream, autoFlush: Boolean) = this(new OutputStreamWriter(out), autoFlush)

  def this(out: OutputStream) = this(out, false)

  def this(fileName: String) = this(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8), false)
}

/** The usual static methods of java.nio.file.Files, on the files of the world (the options of writing can only be APPEND) */
object Files {
  def readAllBytes(path: Path): Array[Byte] = {
    val in = new FileInputStream(path.toString)
    try in.readAllBytes()
    finally in.close()
  }

  def readString(path: Path): String = new String(readAllBytes(path), StandardCharsets.UTF_8)

  def readAllLines(path: Path): java.util.List[String] = readString(path).lines().toList

  def write(path: Path, bytes: Array[Byte], options: OpenOption*): Path = {
    val out = new FileOutputStream(path.toString, options.contains(StandardOpenOption.APPEND))
    try out.write(bytes)
    finally out.close()
    path
  }

  def writeString(path: Path, text: CharSequence, options: OpenOption*): Path = write(path, text.toString.getBytes(StandardCharsets.UTF_8), options*)

  def delete(path: Path): Unit =
    if (!deleteIfExists(path))
      throw new NoSuchFileException(path.toString)

  def deleteIfExists(path: Path): Boolean = plm_remote_unlink(path.toString) == 0
}
