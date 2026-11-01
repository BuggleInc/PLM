/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks

/* BEGIN IMPORT */
import java.net._
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern
import scala.util.Using
/* END IMPORT */

class UdpEchoEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  def server(port: Int): Unit = {
    /* BEGIN SOLUTION */
    val bob = Pattern.compile("PORT <([0-9]+)>")
    Using.resource(new DatagramSocket(port)) { socket =>
      val buffer = new Array[Byte](65507) // the largest possible datagram
      var done   = false
      while (!done) {
        val packet = new DatagramPacket(buffer, buffer.length) // a new one each time, as receive() shortens it
        socket.receive(packet)
        socket.send(new DatagramPacket(buffer, packet.getLength, packet.getSocketAddress))
        val found = bob.matcher(new String(buffer, 0, packet.getLength, StandardCharsets.US_ASCII))
        if (found.find()) {
          println(found.group(1))
          done = true
        }
      }
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(): Unit = server(getParamInt(0))
  /* END REMOTE */
}
