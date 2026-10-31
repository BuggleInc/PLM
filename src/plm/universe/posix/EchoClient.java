package plm.universe.posix;

import java.text.MessageFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * A client of the UDP echo server of the student, on its own machine: it sends each of its lines in a datagram at its time (in
 * seconds), and waits for their echoes before sending the next ones. The lines of a same time are sent in a burst, before
 * waiting for their echoes. A client may check that each echo is exactly its line: on a wrong echo, it sends a complaint
 * instead of its next lines, and stops. Without echo, it waits forever, so its next lines never come.
 */
public final class EchoClient extends ProgramPeer {
  /** A line, sent at that time */
  private record Line(int time, String text) {}

  /** The port of the echo server, on the student's machine */
  private final int port;
  private final List<Line> lines = new ArrayList<>();
  /** The pattern of the complaint about a wrong echo ({0} the bytes sent, {1} the bytes received), or null if not checking */
  private String complaint;
  private int fd;
  /** The next line to send */
  private int next;
  /** The lines sent whose echo did not arrive yet, in order */
  private final Deque<String> awaited = new ArrayDeque<>();
  /** The time of the last burst sent */
  private int burst = -1;
  /** Whether the client complained about a wrong echo, which ends its lines */
  private boolean complained;
  /** Whether the client closed its socket, after its last echo or after complaining */
  private boolean done;

  /** A client called name (in the trace) on ip, of the echo server listening on that port of the student's machine */
  public EchoClient(String ip, String name, int port)
  {
    super(ip, name);
    this.port = port;
  }

  private EchoClient(EchoClient other, Map<FileDescription, FileDescription> copies)
  {
    super(other, copies);
    port = other.port;
    lines.addAll(other.lines); // never changed once the scenario is given
    complaint = other.complaint;
    fd        = other.fd;
    next      = other.next;
    awaited.addAll(other.awaited);
    burst      = other.burst;
    complained = other.complained;
    done       = other.done;
  }

  @Override protected EchoClient copy(Map<FileDescription, FileDescription> copies) { return new EchoClient(this, copies); }

  /** Adds a line, sent in ASCII at that time (from 1, once the student bound its socket, and after the previous lines) */
  public EchoClient says(int time, String text)
  {
    lines.add(new Line(time, ascii(text)));
    return this;
  }

  /**
   * Checks the echoes: on a wrong one, sends this complaint (a MessageFormat pattern whose {0} is the number of bytes sent, and
   * {1} the number of bytes received) and stops
   */
  public EchoClient complaining(String pattern)
  {
    complaint = pattern;
    return this;
  }

  @Override protected boolean documented() { return false; }

  @Override protected void start() { fd = datagramSocket(); }

  @Override protected boolean act()
  {
    if (done)
      return false;
    if (!complained && next < lines.size() && lines.get(next).time() <= now() && (awaited.isEmpty() || lines.get(next).time() == burst)) {
      Line line = lines.get(next++);
      sendDatagram(fd, line.text(), PosixWorld.STUDENT_IP, port);
      awaited.add(line.text());
      burst = line.time();
      return true;
    }
    if (!awaited.isEmpty()) {
      Datagram echo = receiveDatagram(fd);
      if (echo == null)
        return false;
      String sent = awaited.poll();
      if (complaint != null && !echo.data().equals(sent)) {
        String text = MessageFormat.format(complaint, String.valueOf(sent.length()), String.valueOf(echo.data().length()));
        sendDatagram(fd, ascii(text), PosixWorld.STUDENT_IP, port);
        awaited.clear();
        complained = true;
      }
      return true;
    }
    if (next < lines.size() && !complained)
      return false;
    close(fd);
    done = true;
    return true;
  }

  /** The time of the next line, when the client waits for nothing else */
  @Override protected int timer() { return !done && !complained && awaited.isEmpty() && next < lines.size() ? lines.get(next).time() : -1; }

  /** Its state for the deadlocks, without the whole data that it received, which its long lines would make unreadable */
  @Override String describe()
  {
    return name() + " " + process.ip + (waiting() == null ? "" : " (" + waiting() + ")") + ", " + next + " of its " + lines.size() + " lines sent" +
        (complained ? ", then a complaint about a wrong echo" : "");
  }

  @Override protected String waiting()
  {
    if (done)
      return null;
    if (!awaited.isEmpty())
      return "waiting for the echo of " + PosixWorld.excerpt(awaited.peek());
    return next < lines.size() && !complained ? "sleeping until t=" + lines.get(next).time() : null;
  }
}
