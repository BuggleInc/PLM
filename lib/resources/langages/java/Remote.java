import static ValueSerializer.*;

import java.awt.Color;
import java.io.*;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Vector;

public abstract class Remote {

  /*
   * The protocol (commands to the PLM, answers from the PLM) travels over a UNIX domain socket whose path is passed as
   * args[0] -- see connect() below, called from the generated Main.main().
   */

  private static BufferedReader protocolIn;
  private static PrintWriter protocolOut;

  public static void connect(String socketPath)
  {
    try {
      SocketChannel channel = SocketChannel.open(StandardProtocolFamily.UNIX);
      channel.connect(UnixDomainSocketAddress.of(Path.of(socketPath)));
      protocolIn  = new BufferedReader(new InputStreamReader(Channels.newInputStream(channel), StandardCharsets.UTF_8));
      protocolOut = new PrintWriter(new OutputStreamWriter(Channels.newOutputStream(channel), StandardCharsets.UTF_8), true);
    } catch (IOException e) {
      System.out.println("Cannot connect to the PLM protocol socket '" + socketPath + "': " + e.getMessage());
      System.exit(1);
    }
  }

  private static String answerBuffer;

  private static void getAnswerLine()
  {
    try {
      answerBuffer = protocolIn.readLine();
    } catch (IOException e) {
      answerBuffer = null;
      System.err.println("IO exception while reading the protocol (reason: " + e.getMessage() + "). Bailing out.");
      System.exit(1);
    }
    //        System.out.println("Student receives: " + answerBuffer);
    System.out.flush();
    if (answerBuffer == null) {
      System.exit(1);
    }
  }

  public static int getAnswerInt()
  {
    getAnswerLine();
    return (int)deserialize(answerBuffer);
  }

  public static boolean getAnswerBoolean()
  {
    getAnswerLine();
    return (boolean)deserialize(answerBuffer);
  }

  public static double getAnswerDouble()
  {
    getAnswerLine();
    return (double)deserialize(answerBuffer);
  }

  public static Color getAnswerColor()
  {
    getAnswerLine();
    return (Color)deserialize(answerBuffer);
  }

  public static String getAnswerString()
  {
    getAnswerLine();
    return (String)deserialize(answerBuffer);
  }

  public static char getAnswerChar()
  {
    getAnswerLine();
    return (char)deserialize(answerBuffer);
  }

  /*
   * Generic functions for objects (such as Point) that are already deserialized correctly by ValueSerializer.java
   * See also LangJavaExternalPrimitiveGenerator.getReturning().
   */
  public static Object getAnswerObject()
  {
    getAnswerLine();
    return deserialize(answerBuffer);
  }

  public static void sendCommand(String opCode, String name, Object... args)
  {
    String command = opCode + " " + serialize(args) + " " + name;

    //      System.out.println("Student sends: " + command);
    System.out.flush();
    protocolOut.println(command);
    protocolOut.flush();
  }

  /* BEGIN UTILS */

  public static String int2str(int n) { return Integer.toString(n); }

  /* END UTILS */

  public abstract void run();

  public static void main(String[] args) { throw new UnsupportedOperationException("Subclasses should provide their own main() method."); }
}