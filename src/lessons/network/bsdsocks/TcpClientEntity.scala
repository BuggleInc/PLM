/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks

/* BEGIN IMPORT */
import java.net._
import scala.util.Using
/* END IMPORT */

class TcpClientEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  def client(server: String, port: Int): Unit = {
    /* BEGIN SOLUTION */
    def connected(): Socket =
      try new Socket(server, port)
      catch {
        case _: ConnectException => // the server is not ready yet: retry one second later
          Thread.sleep(1000)
          connected()
      }

    Using.resource(connected()) { socket =>
      val in     = socket.getInputStream
      val buffer = new Array[Byte](512)
      Iterator.continually(in.read(buffer)).takeWhile(_ != -1).foreach(received => System.out.write(buffer, 0, received))
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(): Unit = client(getParamString(0), getParamInt(1))
  /* END REMOTE */
}
