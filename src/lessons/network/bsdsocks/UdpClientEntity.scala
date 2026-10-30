/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks

/* BEGIN IMPORT */
import java.net._
import java.nio.charset.StandardCharsets
import scala.util.Using
/* END IMPORT */

class UdpClientEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  def client(server: String, port: Int, request: String): Unit = {
    /* BEGIN SOLUTION */
    Using.resource(new DatagramSocket()) { socket =>
      val data = request.getBytes(StandardCharsets.UTF_8)
      socket.send(new DatagramPacket(data, data.length, InetAddress.getByName(server), port))
      val buffer = new Array[Byte](65507) // the largest possible datagram
      val answer = new DatagramPacket(buffer, buffer.length)
      socket.receive(answer)
      System.out.write(buffer, 0, answer.getLength)
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(): Unit = client(getParamString(0), getParamInt(1), getParamString(2))
  /* END REMOTE */
}
