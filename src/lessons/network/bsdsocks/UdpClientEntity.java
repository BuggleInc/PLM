/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks;

/* BEGIN IMPORT */
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
/* END IMPORT */

public class UdpClientEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void client(String server, int port, String request) throws IOException
  {
    /* BEGIN SOLUTION */
    try (DatagramSocket socket = new DatagramSocket()) {
      byte[] data = request.getBytes(StandardCharsets.UTF_8);
      socket.send(new DatagramPacket(data, data.length, InetAddress.getByName(server), port));
      byte[] buffer         = new byte[65507]; // the largest possible datagram
      DatagramPacket answer = new DatagramPacket(buffer, buffer.length);
      socket.receive(answer);
      System.out.write(buffer, 0, answer.getLength());
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run() throws Exception { client(getParamString(0), getParamInt(1), getParamString(2)); }
  /* END REMOTE */
}
