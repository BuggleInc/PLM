import static generated.ValueSerializer.*;

import java.awt.Color;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Vector;

public abstract class Remote {

  /*
   * The protocol (commands to the PLM, answers from the PLM) travels over a TCP socket on 127.0.0.1, whose port is passed as
   * args[0] -- see connect() below, called from the generated Main.main(). The PLM only listens on that address.
   */

  private static BufferedReader protocolIn;
  private static PrintWriter protocolOut;

  public static void connect(String port)
  {
    try {
      SocketChannel channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", Integer.parseInt(port)));
      channel.setOption(StandardSocketOptions.TCP_NODELAY, true); // each command is a short request awaiting its answer
      protocolIn  = new BufferedReader(new InputStreamReader(Channels.newInputStream(channel), StandardCharsets.UTF_8));
      protocolOut = new PrintWriter(new OutputStreamWriter(Channels.newOutputStream(channel), StandardCharsets.UTF_8), true);
    } catch (IOException e) {
      System.out.println("Cannot connect to the PLM protocol on port " + port + ": " + e.getMessage());
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

  public static void sendCommand(String name, Object... args)
  {
    String command = name + " " + serialize(args);

    //      System.out.println("Student sends: " + command);
    System.out.flush();
    protocolOut.println(command);
    protocolOut.flush();
  }

  public abstract void run();

  public static void main(String[] args) { throw new UnsupportedOperationException("Subclasses should provide their own main() method."); }
}