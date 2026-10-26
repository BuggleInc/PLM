package plm.universe.posix;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import plm.core.ui.MainFrame;
import plm.core.ui.WorldView;
import plm.universe.World;

/**
 * Draws the machine of the student's process on the left, and the machines of the peers on the right, mirrored. On a machine,
 * the descriptor table of the process (one row per descriptor) points to the kernel objects (standard streams, files, sockets), and the
 * sockets hang on the ports at the edge of the machine, between which the connections are drawn. The regular files are drawn as
 * file icons below the student's process, with their name and the beginning of their content. The arrow between a descriptor
 * and its object follows the data: towards the process for stdin and the files open for reading, towards the object for stdout,
 * stderr and the files open for writing, both ways for the sockets and the files open for both. The text cut to fit in a box is
 * shown entirely as a tooltip.
 */
public class PosixWorldView extends WorldView {
  private static final long serialVersionUID = 1L;
  private static final int ROW               = 40; // height of a descriptor row
  private static final int LINE              = 16; // height of a line of text
  private static final int CELL              = 12; // size of a cell of the queues of the sockets
  private static final int FILE_WIDTH        = 90; // size of the icon of a file
  private static final int FILE_HEIGHT       = 55;
  private static final Color PROCESS         = new Color(235, 240, 250); // light blue
  private static final Color KERNEL          = new Color(240, 230, 250); // light violet: the objects of the kernel
  private static final Color NETWORK         = new Color(0, 120, 60);    // dark green: the ports and the connections
  private static final Color HIGHLIGHT       = new Color(200, 40, 40);   // red: the directions of the queues

  /** A box whose text was cut, and its full text */
  private record Tip(Rectangle area, String text) {}
  /** The boxes of the last painting whose text was cut */
  private final List<Tip> tips = new ArrayList<>();
  /** A drawing that opens a text of the world in a read-only tab of the editor when clicked: a file, or the history of a socket */
  private record Link(Rectangle area, String title, Supplier<String> content) {}
  /** The links of the last painting */
  private final List<Link> links = new ArrayList<>();

  public PosixWorldView(World w)
  {
    super(w);
    setToolTipText(""); // registers the view to the tooltip manager, that then asks getToolTipText(MouseEvent)
    MouseAdapter mouse = new MouseAdapter() {
      @Override public void mouseClicked(MouseEvent event)
      {
        Link link = linkAt(event);
        if (link != null)
          MainFrame.getInstance().getEditorTabs().showReadOnly(link.title(), world, link.content());
      }

      @Override public void mouseMoved(MouseEvent event)
      {
        setCursor(Cursor.getPredefinedCursor(linkAt(event) == null ? Cursor.DEFAULT_CURSOR : Cursor.HAND_CURSOR));
      }
    };
    addMouseListener(mouse);
    addMouseMotionListener(mouse);
  }

  private Link linkAt(MouseEvent event)
  {
    for (Link link : links)
      if (link.area().contains(event.getPoint()))
        return link;
    return null;
  }

  @Override public String getToolTipText(MouseEvent event)
  {
    for (int i = tips.size() - 1; i >= 0; i--) // the last drawn is on top, as a cell over the queue that contains it
      if (tips.get(i).area().contains(event.getPoint()))
        return tips.get(i).text();
    return null;
  }

  /**
   * A descriptor of a machine: its number, the lines of the box of its object (null for a free descriptor), the queues of a
   * socket, the direction of its data, and its port (null if none, "" for an anonymous one). The key is the socket, to draw the
   * connections between the machines.
   */
  private record Row(int fd, String[] lines, List<Queue> queues, boolean reads, boolean writes, String port, Object key)
  {
    Row(int fd, String[] lines, boolean reads, boolean writes)
    {
      this(fd, lines, List.of(), reads, writes, null, null);
    }
  }

  /**
   * A queue of a socket: its chunks (of data, or of pending connections), whether the end of the stream follows them, whether it
   * flows towards the network (or towards the process), and its capacity (0 if unbounded) whose free places are drawn empty.
   */
  private record Queue(List<String> chunks, boolean eof, boolean outgoing, int capacity, String role) {}

  private static final String SEND    = "Send queue: the data sent by this process waits here before travelling to the peer";
  private static final String RECEIVE = "Receive queue: the data from the peer waits here until this process receives it";
  private static final String BACKLOG = "Backlog: the connected clients wait here until accept() takes them";

  /** The row of a standard stream: its single queue holds what waits on the input, or what was written on the outputs */
  private static Row stream(int fd, StandardStream stream)
  {
    String output = stream.name.equals("stdout") ? "output" : "error";
    String role   = stream.input ? "Standard input: its data waits here until this process reads it"
                                 : "Standard " + output + ": everything this process wrote on it, displayed in the console";
    Queue queue   = new Queue(stream.data.isEmpty() ? List.of() : List.of(stream.data), stream.input, !stream.input, 0, role);
    return new Row(fd, new String[] {stream.name}, List.of(queue), stream.input, !stream.input, null, null);
  }

  @Override public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    PosixWorld w  = (PosixWorld)world;
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    g2.setColor(Color.white);
    g2.fillRect(0, 0, getWidth(), getHeight());
    tips.clear();
    links.clear();
    g2.setColor(Color.black);
    g2.drawString("t = " + w.now + "s", (getWidth() - 40) / 2, 18);

    // The student's machine on the left, and the peers' machines on the right, mirrored
    Map<Object, Point> ports = new HashMap<>();
    int machineWidth         = getWidth() * 45 / 100;
    machine(g2, 10, machineWidth, 25, "Your machine (" + PosixWorld.STUDENT_IP + ")", rows(w.student), w.files, false, ports);
    int top = 25;
    for (Peer p : w.peers) {
      top = machine(g2, getWidth() - 10 - machineWidth, machineWidth, top, p.title(), rows(p.process), Map.of(), true, ports) + 20;
    }

    // The connections between the ports, dashed while waiting in a backlog
    g2.setColor(NETWORK);
    for (PosixProcess process : w.processes())
      for (FileDescription o : process.fds)
        if (o instanceof Socket s) {
          if (s.remote != null)
            wire(g2, ports.get(s), ports.get(s.remote), false);
          for (Socket pending : s.backlog)
            wire(g2, ports.get(s), ports.get(pending.remote), true);
        }
  }

  /** The rows of the descriptors of a process */
  private static List<Row> rows(PosixProcess process)
  {
    List<Row> rows = new ArrayList<>();
    for (int fd = 0; fd < process.fds.size(); fd++)
      rows.add(row(fd, process.fds.get(fd)));
    return rows;
  }

  /** The row of a descriptor of a process */
  private static Row row(int fd, FileDescription o)
  {
    if (o instanceof StandardStream stream)
      return stream(fd, stream);
    if (o instanceof RegularFile f)
      return new Row(fd, new String[] {"file " + f.path, (f.writable ? "w" : "r") + ", offset " + f.offset}, f.readable, f.writable);
    if (o instanceof Socket s) {
      String port = s.port >= 0 ? String.valueOf(s.port) : s.remote != null ? "" : null; // a client socket gets an anonymous port
      if (s.remote != null) { // the end of the stream waits in the send queue until its data was transmitted
        List<String> sent = s.outgoing.isEmpty() ? List.of() : List.of(s.outgoing.toString());
        return new Row(fd, new String[] {"socket"}, connection(sent, s.writeClosed && !s.outgoing.isEmpty(), new ArrayList<>(s.incoming), s.eofArrived()), true,
                       true, port, s);
      }
      if (s.listening) { // one place per connection that may wait to be accepted
        Queue backlog = new Queue(new ArrayList<>(Collections.nCopies(s.backlog.size(), "•")), false, false, s.backlogMax, BACKLOG);
        return new Row(fd, new String[] {"socket"}, List.of(backlog), true, true, port, s);
      }
      return new Row(fd, new String[] {"socket", s.port >= 0 ? "bound" : "unconnected"}, List.of(), true, true, port, s);
    }
    return new Row(fd, null, false, false);
  }

  /** The queues of a connected socket: the sending one, and the receiving one, each followed by the end of the stream if needed */
  private static List<Queue> connection(List<String> sent, boolean sentEof, List<String> received, boolean receivedEof)
  {
    return List.of(new Queue(sent, sentEof, true, 0, SEND), new Queue(received, receivedEof, false, 0, RECEIVE));
  }

  /**
   * Draws a machine: its process with one row per descriptor, the objects of the descriptors, and the ports on its edge (on the
   * right, or on the left if mirrored), followed by its files (name and content). Stores the position of the port of each keyed
   * row in ports, and returns the bottom of the drawing.
   */
  private int machine(Graphics2D g2, int left, int width, int top, String title, List<Row> rows, Map<String, String> files, boolean mirrored,
                      Map<Object, Point> ports)
  {
    int rowsTop = top + 15;
    int bottom  = rowsTop + rows.size() * ROW + (files.isEmpty() ? 0 : FILE_HEIGHT + 10) + 10;
    g2.setColor(Color.black);
    g2.drawString(title, left + 5, top - 7);
    g2.drawRoundRect(left, top, width, bottom - top, 15, 15);

    // The positions along the machine, mirrored if needed: process, descriptors, objects, edge with the ports
    int processLeft  = mirrored ? left + width * 70 / 100 : left + 10;
    int processWidth = width * 30 / 100 - 10;
    int fdX          = mirrored ? processLeft : processLeft + processWidth;
    int objectLeft   = mirrored ? left + 25 : left + width * 42 / 100;
    int objectWidth  = width * 58 / 100 - 25;
    int objectNear   = mirrored ? objectLeft + objectWidth : objectLeft; // the side of the objects facing the process
    int objectFar    = mirrored ? objectLeft : objectLeft + objectWidth;
    int edge         = mirrored ? left : left + width;
    int toObject     = mirrored ? -1 : 1; // the direction from the process to the objects
    box(g2, PROCESS, processLeft, rowsTop - 5, processWidth, rows.size() * ROW, "Process");

    Map<String, Integer> portRows = new HashMap<>(); // the row of the first socket using each numbered port
    for (Row row : rows) {
      int y = rowCenter(rowsTop, row.fd());
      descriptor(g2, fdX, y, row.fd(), row.lines() != null, mirrored);
      if (row.lines() == null)
        continue;
      g2.setColor(Color.black);
      g2.drawLine(fdX + 6 * toObject, y, objectNear, y);
      if (row.writes())
        arrowHead(g2, objectNear, y, toObject);
      if (row.reads())
        arrowHead(g2, fdX + 6 * toObject, y, -toObject);
      box(g2, KERNEL, objectLeft, y - ROW / 2 + 3, objectWidth, ROW - 6, row.lines());
      if (row.key() instanceof Socket s && s.remote != null) {
        Rectangle area = new Rectangle(objectLeft, y - ROW / 2 + 3, objectWidth, ROW - 6);
        links.add(new Link(area, title + ", socket " + row.fd(), () -> history(s)));
        tips.add(new Tip(area, "Click to see everything that went through this socket"));
      }
      for (int i = 0; i < row.queues().size(); i++) { // on the right of the title, one under the other, a lone queue under the title
        Queue q  = row.queues().get(i);
        int line = row.queues().size() == 1 ? 1 : i;
        queue(g2, objectLeft + 50, y - ROW / 2 + 6 + line * (CELL + 3), objectWidth - 55, q, q.outgoing() != mirrored);
      }
      if (row.port() == null)
        continue;
      int portY = row.port().isEmpty() ? y : rowCenter(rowsTop, portRows.computeIfAbsent(row.port(), number -> row.fd()));
      g2.setColor(NETWORK);
      g2.drawLine(objectFar, y, edge - 6 * toObject, portY);
      port(g2, edge, portY, row.port(), mirrored);
      if (row.port().isEmpty())
        tips.add(new Tip(new Rectangle(edge - 8, portY - 8, 16, 16),
                         "<html>Ephemeral port, chosen by the system during connect():<br>a client needs no fixed port, as nobody connects to it</html>"));
      if (row.key() != null)
        ports.put(row.key(), new Point(edge, portY));
    }

    int x = left + 10;
    for (Map.Entry<String, String> file : files.entrySet()) {
      file(g2, x, rowsTop + rows.size() * ROW + 5, file.getKey(), file.getValue());
      x += FILE_WIDTH + 10;
    }
    return bottom;
  }

  /** Everything that went through a connected socket: what arrived on it (received or not), and what it sent (transmitted or not) */
  private static String history(Socket s) { return "Received:\n" + s.arrived + "\n\nSent:\n" + s.remote.arrived + s.outgoing; }

  /** Draws a file as an icon with a folded corner: its name on top, then the first lines of its content (all of it as a tooltip) */
  private void file(Graphics2D g2, int x, int y, String name, String content)
  {
    int fold = 10;
    int[] xs = {x, x + FILE_WIDTH - fold, x + FILE_WIDTH, x + FILE_WIDTH, x};
    int[] ys = {y, y, y + fold, y + FILE_HEIGHT, y + FILE_HEIGHT};
    g2.setColor(Color.white);
    g2.fillPolygon(xs, ys, 5);
    g2.setColor(Color.black);
    g2.drawPolygon(xs, ys, 5);
    g2.drawPolyline(new int[] {x + FILE_WIDTH - fold, x + FILE_WIDTH - fold, x + FILE_WIDTH}, new int[] {y, y + fold, y + fold}, 3);
    g2.drawString(fit(g2, name, FILE_WIDTH - fold - 6), x + 4, y + LINE - 3);
    g2.drawLine(x, y + LINE + 1, x + FILE_WIDTH, y + LINE + 1);

    Font font = g2.getFont();
    g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
    String[] lines = content.split("\n", -1);
    int shown      = Math.min(lines.length, (FILE_HEIGHT - LINE - 4) / 11);
    for (int i = 0; i < shown; i++) {
      String line = i == shown - 1 && shown < lines.length ? "…" : PosixWorld.visible(lines[i]);
      g2.drawString(fit(g2, line, FILE_WIDTH - 6), x + 3, y + LINE + 12 + 11 * i);
    }
    g2.setFont(font);
    Rectangle area = new Rectangle(x, y, FILE_WIDTH, FILE_HEIGHT);
    tips.add(new Tip(area, "<html>" + escape(name) + ":<br><pre>" + escape(content) + "</pre><i>Click to open it in the editor</i></html>"));
    links.add(new Link(area, name, () -> ((PosixWorld)world).files.getOrDefault(name, "(this file does not exist anymore)")));
  }

  private static String escape(String text) { return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"); }

  private static int rowCenter(int top, int fd) { return top + fd * ROW + ROW / 2 - 5; }

  private void descriptor(Graphics2D g2, int x, int y, int fd, boolean used, boolean mirrored)
  {
    g2.setColor(used ? PROCESS : Color.white);
    g2.fillOval(x - 6, y - 6, 12, 12);
    g2.setColor(used ? Color.black : Color.lightGray);
    g2.drawOval(x - 6, y - 6, 12, 12);
    g2.drawString(String.valueOf(fd), mirrored ? x + 10 : x - 22, y + 4);
  }

  /**
   * Draws a queue as cells of one byte each (or one connection), the chunks separated by thicker lines, followed by EOF if needed.
   * The cells keep the reading order, and stick to the side where they leave, which an arrow shows. A queue too long for its
   * width is cut, with its full content in a tooltip.
   */
  private void queue(Graphics2D g2, int x, int y, int width, Queue q, boolean leavesRight)
  {
    List<String> cells   = new ArrayList<>();
    List<Integer> starts = new ArrayList<>(); // the cells starting a new chunk
    for (String chunk : q.chunks()) {
      starts.add(cells.size());
      for (char c : chunk.toCharArray())
        cells.add(c == '\n' ? "⏎" : c == '\r' ? "↵" : c == '\t' ? "⇥" : String.valueOf(c));
    }
    int used = cells.size();
    while (cells.size() < q.capacity())
      cells.add(null); // a free place
    if (q.eof())
      cells.add("EOF");

    int fit    = Math.max(1, (width - 10) / CELL);
    int hidden = 0;
    if (cells.size() > fit) {
      hidden            = cells.size() - fit + 1;
      cells             = cells.subList(0, fit - 1);
      StringBuilder all = new StringBuilder();
      for (String chunk : q.chunks())
        all.append(chunk);
      tips.add(new Tip(new Rectangle(x, y, width, CELL), "'" + PosixWorld.visible(all.toString()) + "'" + (q.eof() ? " then EOF" : "")));
    }
    int cellsWidth = cells.isEmpty() ? CELL // the place of an empty queue
                                     : cells.stream().mapToInt(c -> "EOF".equals(c) ? 2 * CELL : CELL).sum() + (hidden > 0 ? 2 * CELL : 0);
    int cx         = leavesRight ? x + width - 10 - cellsWidth : x + 10;

    g2.setColor(HIGHLIGHT);
    arrowHead(g2, leavesRight ? x + width : x, y + CELL / 2, leavesRight ? 1 : -1);
    tips.add(new Tip(new Rectangle(leavesRight ? x + width - 9 : x - 1, y, 10, CELL), q.role()));
    Font font = g2.getFont();
    g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
    for (int i = 0; i < cells.size(); i++) {
      String cell = cells.get(i);
      int w       = "EOF".equals(cell) ? 2 * CELL : CELL;
      g2.setColor(i < used ? Color.white : KERNEL);
      g2.fillRect(cx, y, w, CELL);
      g2.setColor(Color.black);
      g2.drawRect(cx, y, w, CELL);
      if (starts.contains(i) && i > 0)
        g2.fillRect(cx - 1, y, 3, CELL + 1); // a chunk boundary: recv() never returns more than one chunk
      if (cell != null)
        g2.drawString(cell, cx + (w - g2.getFontMetrics().stringWidth(cell)) / 2, y + CELL - 2);
      String meaning = null;
      if ("EOF".equals(cell)) {
        if (q.role() == RECEIVE)
          meaning = "End of stream: the peer closed its side after this data. Once all the data is received, recv() returns 0";
        else if (q.role() == SEND)
          meaning = "End of stream: this process closed its side, and the end travels to the peer after this data";
        else
          meaning = "End of the input: once all the data is read, read() returns 0";
      } else if (q.capacity() > 0)
        meaning = cell == null ? "A free place: listen() allows that many clients to wait" : "A client that connected and waits for accept()";
      if (meaning != null)
        tips.add(new Tip(new Rectangle(cx, y, w, CELL), meaning));
      cx += w;
    }
    if (hidden > 0)
      g2.drawString("+" + hidden, cx + 2, y + CELL - 2);
    if (cells.isEmpty()) { // an empty queue still shows its place
      g2.setColor(Color.lightGray);
      g2.drawRect(cx, y, CELL, CELL);
    }
    g2.setFont(font);
  }

  /** Draws the head of a horizontal arrow ending at (x, y), pointing right (direction 1) or left (direction -1) */
  private static void arrowHead(Graphics2D g2, int x, int y, int direction)
  {
    g2.fillPolygon(new int[] {x, x - 8 * direction, x - 8 * direction}, new int[] {y, y - 4, y + 4}, 3);
  }

  private void port(Graphics2D g2, int x, int y, String number, boolean mirrored)
  {
    g2.setColor(Color.white);
    g2.fillOval(x - 6, y - 6, 12, 12);
    g2.setColor(NETWORK);
    g2.drawOval(x - 6, y - 6, 12, 12);
    g2.drawString(number, mirrored ? x - 8 - g2.getFontMetrics().stringWidth(number) : x + 8, y - 8);
  }

  /** Draws the connection between two ports (if both still exist) from the outside of their circles, dashed while it waits in a backlog */
  private static void wire(Graphics2D g2, Point a, Point b, boolean pending)
  {
    if (a == null || b == null)
      return;
    Point left = a.x < b.x ? a : b, right = a.x < b.x ? b : a;
    if (pending)
      g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[] {4, 4}, 0));
    g2.drawLine(left.x + 6, left.y, right.x - 6, right.y);
    g2.setStroke(new BasicStroke());
  }

  /** Draws a box with a title and some lines of text, cut to fit in it (the full text is then shown as a tooltip) */
  private void box(Graphics2D g2, Color fill, int x, int y, int width, int height, String... lines)
  {
    g2.setColor(fill);
    g2.fillRoundRect(x, y, width, height, 10, 10);
    g2.setColor(Color.black);
    g2.drawRoundRect(x, y, width, height, 10, 10);
    boolean cut = false;
    for (int i = 0; i < lines.length; i++) {
      String shown = fit(g2, lines[i], width - 10);
      cut |= !shown.equals(lines[i]);
      g2.drawString(shown, x + 5, y + LINE * (i + 1) - 3);
    }
    if (cut) {
      StringBuilder html = new StringBuilder("<html>");
      for (String line : lines)
        html.append(escape(line)).append("<br>");
      tips.add(new Tip(new Rectangle(x, y, width, height), html.append("</html>").toString()));
    }
  }

  private static String fit(Graphics2D g2, String text, int width)
  {
    if (g2.getFontMetrics().stringWidth(text) <= width)
      return text;
    while (text.length() > 1 && g2.getFontMetrics().stringWidth(text + "…") > width)
      text = text.substring(0, text.length() - 1);
    return text + "…";
  }
}
