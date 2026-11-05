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
  public void client(String server, int port, String room, String password) throws IOException
  {
    /* BEGIN SOLUTION */
    /* END SOLUTION */
  }
  /* END TEMPLATE */

  public void run() throws Exception { client(getParamString(0), getParamInt(1), getParamString(2), getParamString(3)); }
  /* END REMOTE */
}
