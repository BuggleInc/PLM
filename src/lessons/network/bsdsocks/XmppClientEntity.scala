/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks

/* BEGIN IMPORT */
import java.io._
import java.net._
import java.nio.charset.StandardCharsets
import java.util.Base64
/* END IMPORT */

class XmppClientEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  def client(server: String, port: Int, room: String, password: String): Unit = {
    /* BEGIN SOLUTION */
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(): Unit = client(getParamString(0), getParamInt(1), getParamString(2), getParamString(3))
  /* END REMOTE */
}
