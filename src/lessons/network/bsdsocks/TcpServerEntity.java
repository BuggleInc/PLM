/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks;

/* BEGIN IMPORT */
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
/* END IMPORT */

public class TcpServerEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void server(int port, String message) throws IOException
  {
    /* BEGIN SOLUTION */
    try (ServerSocket master = new ServerSocket(port); Socket client = master.accept()) {
      client.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run() throws Exception { server(getParamInt(0), getParamString(1)); }
  /* END REMOTE */
}
