/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks;

/* BEGIN IMPORT */
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.*;
/* END IMPORT */

public class UdpEchoEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void server(int port) throws IOException
  {
    /* BEGIN SOLUTION */
    Pattern bob = Pattern.compile("PORT <([0-9]+)>");
    try (DatagramSocket socket = new DatagramSocket(port)) {
      byte[] buffer = new byte[65507]; // the largest possible datagram
      while (true) {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length); // a new one each time, as receive() shortens it
        socket.receive(packet);
        socket.send(new DatagramPacket(buffer, packet.getLength(), packet.getSocketAddress()));
        Matcher found = bob.matcher(new String(buffer, 0, packet.getLength(), StandardCharsets.US_ASCII));
        if (found.find()) {
          System.out.println(found.group(1));
          return;
        }
      }
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run() throws Exception { server(getParamInt(0)); }
  /* END REMOTE */
}
