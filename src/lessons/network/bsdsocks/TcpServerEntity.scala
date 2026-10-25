/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks

/* BEGIN IMPORT */
import java.io._
import java.net._
import java.nio.charset.StandardCharsets
import scala.util.Using
/* END IMPORT */

class TcpServerEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  def server(port: Int): Unit = {
    /* BEGIN SOLUTION */
    Using.Manager { use =>
      val master = use(new ServerSocket(port))
      val client = use(master.accept())
      // The line may arrive in several packets: readLine() receives until its '\n' (or until the client closes the connection)
      val line = new BufferedReader(new InputStreamReader(client.getInputStream, StandardCharsets.UTF_8)).readLine()
      if (line != null)
        client.getOutputStream.write(((line + "\n") * 2).getBytes(StandardCharsets.UTF_8)) // the doubling echo
    }.get
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(): Unit = server(getParamInt(0))
  /* END REMOTE */
}
