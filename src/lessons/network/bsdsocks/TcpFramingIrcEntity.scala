/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks

/* BEGIN IMPORT */
import java.io._
import java.net._
import java.nio.charset.StandardCharsets
/* END IMPORT */

class TcpFramingIrcEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */

  /* BEGIN SOLUTION */
  /** The first line containing msg (without its \r\n), or null if the stream ends before */
  def waitline(in: BufferedReader, msg: String): String =
    Iterator.continually(in.readLine()).takeWhile(_ != null).find(_.contains(msg)).orNull
  /* END SOLUTION */

  def client(): Unit = {
    /* BEGIN SOLUTION */
    val socket = new Socket("10.0.0.1", 194)
    val in     = new BufferedReader(new InputStreamReader(socket.getInputStream, StandardCharsets.UTF_8))
    val out    = socket.getOutputStream

    out.write("NICK student\r\nUSER student 0 * :Student\r\n".getBytes(StandardCharsets.UTF_8))
    waitline(in, "001")

    out.write("JOIN #welcome\r\n".getBytes(StandardCharsets.UTF_8))
    waitline(in, "366")

    out.write("PRIVMSG #welcome :Bob: Please tell me where the documentation is.\r\n".getBytes(StandardCharsets.UTF_8))
    val line = waitline(in, "/docs/")

    val path = line.substring(line.indexOf('/'))
    print(path.substring(0, path.indexOf(' ')))
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(): Unit = client()
  /* END REMOTE */
}
