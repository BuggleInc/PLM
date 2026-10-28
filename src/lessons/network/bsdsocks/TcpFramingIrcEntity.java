/* This file is never compiled directly but only serves as a template to the compilation of student's code */
package lessons.network.bsdsocks;

/* BEGIN IMPORT */
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
/* END IMPORT */

public class TcpFramingIrcEntity extends plm.universe.posix.PosixEntity {
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */

  /* BEGIN SOLUTION */
  String waitline(BufferedReader in, String msg) throws IOException, InterruptedException
  {
    String line;
    while ((line = in.readLine()) != null) // without its \r\n
      if (line.contains(msg))
        return line;
    return null;
  }
  /* END SOLUTION */

  public void client() throws IOException, InterruptedException
  {
    /* BEGIN SOLUTION */
    Socket socket     = new Socket("10.0.0.1", 194);
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    OutputStream out  = socket.getOutputStream();

    out.write("NICK student\r\nUSER student 0 * :Student\r\n".getBytes(StandardCharsets.UTF_8));

    waitline(in, "001");

    out.write("JOIN #welcome\r\n".getBytes(StandardCharsets.UTF_8));

    waitline(in, "366");

    out.write("PRIVMSG #welcome :Bob: Please tell me where the documentation is.\r\n".getBytes(StandardCharsets.UTF_8));
    String line = waitline(in, "/docs/");

    int begin = line.indexOf('/');
    line      = line.substring(begin);
    line      = line.substring(0, line.indexOf(' '));

    System.out.println(line);
    socket.close(); // disconnect from the server
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run() throws Exception { client(); }
  /* END REMOTE */
}
