import java.awt.Color
import java.io._
import java.net.InetSocketAddress
import java.net.StandardSocketOptions
import java.nio.channels.Channels
import java.nio.channels.SocketChannel
import java.nio.charset.StandardCharsets

import ValueSerializer._

/*
 * The protocol (commands to the PLM, answers from the PLM) travels over a TCP socket on 127.0.0.1, whose port is passed as
 * args(0) -- see connect() below, called from the generated Main.main(). The PLM only listens on that address.
 *
 * Mirrors Remote.java member for member; see that file for the authoritative comments on each piece. Kept in sync by hand
 * for now (LangJava and LangScala are not yet factored together).
 */
object Remote {

  private var protocolIn: BufferedReader = null
  private var protocolOut: PrintWriter   = null

  def connect(port: String): Unit =
  {
    try {
      val channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", port.toInt))
      channel.setOption[java.lang.Boolean](StandardSocketOptions.TCP_NODELAY, true) // each command is a short request awaiting its answer
      protocolIn = new BufferedReader(new InputStreamReader(Channels.newInputStream(channel), StandardCharsets.UTF_8))
      protocolOut = new PrintWriter(new OutputStreamWriter(Channels.newOutputStream(channel), StandardCharsets.UTF_8), true)
    } catch {
      case e: IOException =>
        println("Cannot connect to the PLM protocol on port " + port + ": " + e.getMessage)
        System.exit(1)
    }
  }

  private var answerBuffer: String = null

  private def getAnswerLine(): Unit =
  {
    try {
      answerBuffer = protocolIn.readLine()
    } catch {
      case e: IOException =>
        answerBuffer = null
        System.err.println("IO exception while reading the protocol (reason: " + e.getMessage + "). Bailing out.")
        System.exit(1)
    }
    System.out.flush()
    if (answerBuffer == null) {
      System.exit(1)
    }
  }

  def getAnswerInt(): Int =
  {
    getAnswerLine()
    deserialize(answerBuffer).asInstanceOf[Int]
  }

  def getAnswerBoolean(): Boolean =
  {
    getAnswerLine()
    deserialize(answerBuffer).asInstanceOf[Boolean]
  }

  def getAnswerDouble(): Double =
  {
    getAnswerLine()
    deserialize(answerBuffer).asInstanceOf[Double]
  }

  def getAnswerColor(): Color =
  {
    getAnswerLine()
    deserialize(answerBuffer).asInstanceOf[Color]
  }

  def getAnswerString(): String =
  {
    getAnswerLine()
    deserialize(answerBuffer).asInstanceOf[String]
  }

  def getAnswerChar(): Char =
  {
    getAnswerLine()
    deserialize(answerBuffer).asInstanceOf[Char]
  }

  /* Generic escape hatch for return types Remote.scala can't name -- see Remote.java's getAnswerObject() javadoc. */
  def getAnswerObject(): Object =
  {
    getAnswerLine()
    deserialize(answerBuffer)
  }

  def sendCommand(name: String, args: Object*): Unit =
  {
    val command = name + " " + serialize(args.toArray)

    System.out.flush()
    protocolOut.println(command)
    protocolOut.flush()
  }
}
