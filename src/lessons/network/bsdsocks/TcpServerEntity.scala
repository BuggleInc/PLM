/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks

/* BEGIN IMPORT */
import java.net._
import java.nio.charset.StandardCharsets
import scala.util.Using
/* END IMPORT */

class TcpServerEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  def server(port: Int, message: String): Unit = {
    /* BEGIN SOLUTION */
    Using.Manager { use =>
      val master = use(new ServerSocket(port))
      val client = use(master.accept())
      client.getOutputStream.write(message.getBytes(StandardCharsets.UTF_8))
    }.get
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(): Unit = server(getParamInt(0), getParamString(1))
  /* END REMOTE */
}
