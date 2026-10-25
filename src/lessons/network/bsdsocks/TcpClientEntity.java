/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks;

/* BEGIN IMPORT */
import java.io.*;
import java.net.*;
/* END IMPORT */

public class TcpClientEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void client(String server, int port) throws IOException, InterruptedException
  {
    /* BEGIN SOLUTION */
    while (true) {
      try (Socket socket = new Socket(server, port)) {
        InputStream in = socket.getInputStream();
        byte[] buffer  = new byte[512];
        int received;
        while ((received = in.read(buffer)) != -1)
          System.out.write(buffer, 0, received);
        return;
      } catch (ConnectException e) { // the server is not ready yet: retry one second later
        Thread.sleep(1000);
      }
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run() throws Exception { client(getParamString(0), getParamInt(1)); }
  /* END REMOTE */
}
