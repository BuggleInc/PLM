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
  public void server(int port) throws IOException
  {
    /* BEGIN SOLUTION */
    try (ServerSocket master = new ServerSocket(port); Socket client = master.accept()) {
      // The line may arrive in several packets: readLine() receives until its '\n' (or until the client closes the connection)
      BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
      String line       = in.readLine();
      if (line != null)
        client.getOutputStream().write((line + "\n").repeat(2).getBytes(StandardCharsets.UTF_8)); // the doubling echo
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run() throws Exception { server(getParamInt(0)); }
  /* END REMOTE */
}
