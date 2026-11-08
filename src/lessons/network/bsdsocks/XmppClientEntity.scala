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
  /* BEGIN SOLUTION */
  private var depth = 0 // 0 before the header of the stream, 1 inside it, 2 or more inside a stanza

  /** The next stanza of the stream, once it arrived whole, or null at the end of the stream */
  def nextStanza(in: PushbackInputStream): String = {
    val stanza   = new StringBuilder
    var kind     = 0 // the byte after the < of the tag being read, or 0 out of a tag
    var previous = 0
    while (true) {
      if (depth == 1 && stanza.isEmpty) { // the spaces between the stanzas
        var c = in.read()
        while (c == ' ' || c == '\t' || c == '\r' || c == '\n')
          c = in.read()
        if (c != -1)
          in.unread(c)
      }
      val c = in.read()
      if (c == -1)
        return null
      stanza.append(c.toChar)
      if (previous == '<')
        kind = c
      else if (c == '>' && kind != 0) { // the end of a tag
        val before = depth
        if (kind == '/') // a closing tag
          depth -= 1
        else if (kind != '?' && previous != '/') // an opening tag, unless the XML declaration or a self-closing tag
          depth += 1
        kind = 0
        if (before == 0) // the XML declaration or the header of the stream, which are no stanzas
          stanza.clear()
        else if (depth == 0) // </stream:stream>
          return null
        else if (depth == 1)
          return stanza.toString
      }
      previous = c
    }
    null
  }

  /** The text of the body of a stanza, or an empty text */
  def body(stanza: String): String = {
    val start = stanza.indexOf("<body>")
    if (start < 0) "" else stanza.substring(start + "<body>".length, stanza.indexOf("</body>"))
  }

  /** Whether the stanza itself (not a message that it quotes) was sent by that address */
  def sentBy(stanza: String, sender: String): Boolean = stanza.substring(0, stanza.indexOf('>')).contains(" from=\"" + sender + "\"")
  /* END SOLUTION */

  def client(server: String, port: Int, room: String, password: String, friend: String): Unit = {
    /* BEGIN SOLUTION */
    val socket = new Socket(server, port)
    val out    = socket.getOutputStream
    val in     = new PushbackInputStream(new BufferedInputStream(socket.getInputStream))
    out.write(("<?xml version='1.0'?><stream:stream to='buggleinc.lan' xmlns='jabber:client' "
                 + "xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>").getBytes(StandardCharsets.US_ASCII))
    nextStanza(in) // the features of the stream

    val credentials = ("\u0000newcomer\u0000" + password).getBytes(StandardCharsets.US_ASCII)
    out.write(("<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='PLAIN'>" + Base64.getEncoder.encodeToString(credentials) + "</auth>")
                .getBytes(StandardCharsets.US_ASCII))
    nextStanza(in) // <success/>

    out.write(s"<presence to='$room/newcomer'><x xmlns='http://jabber.org/protocol/muc'/></presence>".getBytes(StandardCharsets.US_ASCII))
    var stanza = nextStanza(in)
    while (!stanza.contains("<subject>")) // the subject of the room ends the joining
      stanza = nextStanza(in)

    stanza = nextStanza(in)
    while (!sentBy(stanza, s"$room/$friend")) // the friend of Bob calls the newcomer once he is back
      stanza = nextStanza(in)
    out.write(s"<message to='$room' type='groupchat'><body>$friend: Bob sent me for the treasure.</body></message>".getBytes(StandardCharsets.US_ASCII))

    while (!sentBy(stanza, s"$room/$friend") || !body(stanza).contains("[http"))
      stanza = nextStanza(in)
    val text  = body(stanza)
    val start = text.indexOf("[http") + 1
    println(text.substring(start, text.indexOf(']', start)))

    out.write("</stream:stream>".getBytes(StandardCharsets.US_ASCII))
    while (nextStanza(in) != null) {} // up to the end of the stream of the server
    socket.close()
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  override def run(): Unit = client(getParamString(0), getParamInt(1), getParamString(2), getParamString(3), getParamString(4))
  /* END REMOTE */
}
