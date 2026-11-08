/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks;

/* BEGIN IMPORT */
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
/* END IMPORT */

public class XmppClientEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  /* BEGIN SOLUTION */
  private int depth = 0; // 0 before the header of the stream, 1 inside it, 2 or more inside a stanza

  /** The next stanza of the stream, once it arrived whole, or null at the end of the stream */
  String nextStanza(PushbackInputStream in) throws IOException
  {
    StringBuilder stanza = new StringBuilder();
    int kind = 0, previous = 0; // kind: the byte after the < of the tag being read, or 0 out of a tag
    while (true) {
      int c;
      if (depth == 1 && stanza.length() == 0) { // the spaces between the stanzas
        while ((c = in.read()) == ' ' || c == '\t' || c == '\r' || c == '\n')
          ;
        if (c != -1)
          in.unread(c);
      }
      c = in.read();
      if (c == -1)
        return null;
      stanza.append((char)c);
      if (previous == '<')
        kind = c;
      else if (c == '>' && kind != 0) { // the end of a tag
        int before = depth;
        if (kind == '/') // a closing tag
          depth--;
        else if (kind != '?' && previous != '/') // an opening tag, unless the XML declaration or a self-closing tag
          depth++;
        kind = 0;
        if (before == 0) // the XML declaration or the header of the stream, which are no stanzas
          stanza.setLength(0);
        else if (depth == 0) // </stream:stream>
          return null;
        else if (depth == 1)
          return stanza.toString();
      }
      previous = c;
    }
  }

  /** The text of the body of a stanza, or an empty text */
  static String body(String stanza)
  {
    int start = stanza.indexOf("<body>");
    return start < 0 ? "" : stanza.substring(start + "<body>".length(), stanza.indexOf("</body>"));
  }

  /** Whether the stanza itself (not a message that it quotes) was sent by that address */
  static boolean sentBy(String stanza, String sender) { return stanza.substring(0, stanza.indexOf('>')).contains(" from=\"" + sender + "\""); }
  /* END SOLUTION */

  public void client(String server, int port, String room, String password, String friend) throws IOException
  {
    /* BEGIN SOLUTION */
    Socket socket          = new Socket(server, port);
    OutputStream out       = socket.getOutputStream();
    PushbackInputStream in = new PushbackInputStream(new BufferedInputStream(socket.getInputStream()));
    out.write(("<?xml version='1.0'?><stream:stream to='buggleinc.lan' xmlns='jabber:client' "
               + "xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>")
                  .getBytes(StandardCharsets.US_ASCII));
    nextStanza(in); // the features of the stream

    byte[] credentials = ("\0newcomer\0" + password).getBytes(StandardCharsets.US_ASCII);
    out.write(("<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='PLAIN'>" + Base64.getEncoder().encodeToString(credentials) + "</auth>")
                  .getBytes(StandardCharsets.US_ASCII));
    nextStanza(in); // <success/>

    out.write(("<presence to='" + room + "/newcomer'><x xmlns='http://jabber.org/protocol/muc'/></presence>").getBytes(StandardCharsets.US_ASCII));
    String stanza = nextStanza(in);
    while (!stanza.contains("<subject>")) // the subject of the room ends the joining
      stanza = nextStanza(in);

    stanza = nextStanza(in);
    while (!sentBy(stanza, room + "/" + friend)) // the friend of Bob calls the newcomer once he is back
      stanza = nextStanza(in);
    out.write(("<message to='" + room + "' type='groupchat'><body>" + friend + ": Bob sent me for the treasure.</body></message>")
                  .getBytes(StandardCharsets.US_ASCII));

    while (!sentBy(stanza, room + "/" + friend) || !body(stanza).contains("[http"))
      stanza = nextStanza(in);
    String text = body(stanza);
    int start   = text.indexOf("[http") + 1;
    System.out.println(text.substring(start, text.indexOf(']', start)));

    out.write("</stream:stream>".getBytes(StandardCharsets.US_ASCII));
    while (nextStanza(in) != null) // up to the end of the stream of the server
      ;
    socket.close();
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run() throws Exception { client(getParamString(0), getParamInt(1), getParamString(2), getParamString(3), getParamString(4)); }
  /* END REMOTE */
}
