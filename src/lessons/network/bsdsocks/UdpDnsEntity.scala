/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks

/* BEGIN IMPORT */
import java.io.ByteArrayOutputStream
import java.net._
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import scala.collection.mutable.ArrayBuffer
import scala.util.Using
/* END IMPORT */

class UdpDnsEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */

  /* BEGIN SOLUTION */
  val A   = 1
  val TXT = 16
  val SRV = 33

  /** The name in the wire format: each label preceded by its length, then a zero byte */
  def encodeName(name: String): Array[Byte] = {
    val result = new ByteArrayOutputStream()
    for (label <- name.split("\\.")) {
      result.write(label.length)
      result.writeBytes(label.getBytes(StandardCharsets.US_ASCII))
    }
    result.write(0)
    result.toByteArray
  }

  /** The offset after the name written at that offset */
  def skipName(data: Array[Byte], start: Int): Int = {
    var offset = start
    while (data(offset) != 0)
      offset += 1 + data(offset)
    offset + 1
  }

  /** The name written at that offset (never compressed by this server) */
  def readName(data: Array[Byte], start: Int): String = {
    val labels = ArrayBuffer[String]()
    var offset = start
    while (data(offset) != 0) {
      val length = data(offset)
      labels += new String(data, offset + 1, length, StandardCharsets.US_ASCII)
      offset += 1 + length
    }
    labels.mkString(".")
  }

  /** Asks the records of that type of the name, and returns the data of each record of the answer section */
  def query(socket: DatagramSocket, name: String, qtype: Int): ArrayBuffer[Array[Byte]] = {
    val query = new ByteArrayOutputStream()
    query.writeBytes(Array[Byte](0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0)) // ID 1, RD, one question
    query.writeBytes(encodeName(name))
    query.writeBytes(Array[Byte](0, qtype.toByte, 0, 1))               // the type (below 256 here), class IN
    val data = query.toByteArray
    socket.send(new DatagramPacket(data, data.length, InetAddress.getByName("10.0.0.53"), 53))

    val buffer = new Array[Byte](512)
    socket.receive(new DatagramPacket(buffer, buffer.length))
    val answer  = ByteBuffer.wrap(buffer)
    val answers = answer.getShort(6) & 0xFFFF
    var offset  = skipName(buffer, 12) + 4 // after the copy of the question, with its type and class
    val records = ArrayBuffer[Array[Byte]]()
    for (i <- 0 until answers) {
      offset = skipName(buffer, offset)
      val recordType = answer.getShort(offset) & 0xFFFF
      val length     = answer.getShort(offset + 8) & 0xFFFF
      offset += 10
      if (recordType == qtype)
        records += buffer.slice(offset, offset + length)
      offset += length
    }
    records
  }
  /* END SOLUTION */

  def client(): Unit = {
    /* BEGIN SOLUTION */
    Using.resource(new DatagramSocket()) { socket =>
      // The hints followed while exploring, which this program does not need to read: the notes of the zone lead to
      // conference.buggleinc.lan, whose notes lead to the SRV record of the XMPP service
      query(socket, "buggleinc.lan", TXT)
      query(socket, "conference.buggleinc.lan", TXT)

      // The XMPP server of buggleinc.lan with the lowest priority
      var bestPriority = Int.MaxValue
      var bestTarget   = ""
      for (record <- query(socket, "_xmpp-client._tcp.buggleinc.lan", SRV)) {
        val priority = ByteBuffer.wrap(record).getShort(0) & 0xFFFF
        if (priority < bestPriority) {
          bestPriority = priority
          bestTarget   = readName(record, 6)
        }
      }

      val address = query(socket, bestTarget, A)(0)
      println(s"${address(0) & 0xFF}.${address(1) & 0xFF}.${address(2) & 0xFF}.${address(3) & 0xFF}")
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(): Unit = client()
  /* END REMOTE */
}
