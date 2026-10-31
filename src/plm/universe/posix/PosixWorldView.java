package plm.universe.posix;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import plm.core.model.Game;
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
 *
 * The timeline mode draws the successive states of the world instead (see PosixWorld.history), one under the other, linked by
 * arrows labelled by the event that led from one to the next: under the machine whose process made the call, which is the only
 * one drawn plainly, or between the machines for the network. It shows either the system calls only, or all the events. The
 * standard streams can be hidden in both modes. These options, set by the controls above the drawing, are shared by all the
 * views (such as the current world and the objective).
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
  /**
   * A drawing that opens a text of the world in a read-only tab of the editor when clicked: a file, the history of a socket, or a
   * standard output of the student's process
   */
  private record Link(Rectangle area, String title, Supplier<String> content) {}
  /** The links of the last painting */
  private final List<Link> links = new ArrayList<>();

  private static final int GAP      = 34;   // room between two states of the timeline, for the arrow of the event
  private static final float DIMMED = 0.5f; // opacity of the machines that did not act in a state of the timeline

  /** The options of the views, shared by all of them */
  private static boolean showStd = true, timeline = false, allEvents = false;
  /** The views to update when the options change */
  private static final List<PosixWorldView> views = new CopyOnWriteArrayList<>();

  private final Canvas canvas                = new Canvas();
  private final JScrollPane scroll           = new JScrollPane(canvas);
  private final JCheckBox stdBox             = new JCheckBox(Game.i18n.tr("Standard streams"));
  private final JCheckBox timelineBox        = new JCheckBox(Game.i18n.tr("Timeline"));
  private final JComboBox<String> detailsBox = new JComboBox<>(new String[] {Game.i18n.tr("System calls only"), Game.i18n.tr("All events")});

  public PosixWorldView(World w)
  {
    super(w);
    setLayout(new BorderLayout());
    JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
    controls.add(stdBox);
    controls.add(timelineBox);
    controls.add(detailsBox);
    stdBox.setToolTipText(Game.i18n.tr("Show the standard input, output and error of the processes"));
    timelineBox.setToolTipText(Game.i18n.tr("Show the successive states of the world, from the beginning"));
    add(controls, BorderLayout.NORTH);
    scroll.setBorder(null);
    scroll.getVerticalScrollBar().setUnitIncrement(ROW);
    add(scroll, BorderLayout.CENTER);
    stdBox.addActionListener(e -> setOptions(stdBox.isSelected(), timeline, allEvents));
    timelineBox.addActionListener(e -> setOptions(showStd, timelineBox.isSelected(), allEvents));
    detailsBox.addActionListener(e -> setOptions(showStd, timeline, detailsBox.getSelectedIndex() == 1));
    showOptions();
    views.add(this);
  }

  @Override public void dispose()
  {
    views.remove(this);
    super.dispose();
  }

  /** Changes the options of all the views */
  private static void setOptions(boolean std, boolean time, boolean all)
  {
    showStd   = std;
    timeline  = time;
    allEvents = all;
    for (PosixWorldView view : views) {
      view.showOptions();
      view.refresh(time);
    }
  }

  /** Shows the current options in the controls (which only call setOptions() again when a value changes) */
  private void showOptions()
  {
    stdBox.setSelected(showStd);
    timelineBox.setSelected(timeline);
    detailsBox.setSelectedIndex(allEvents ? 1 : 0);
    detailsBox.setEnabled(timeline);
  }

  @Override public void worldHasMoved() { SwingUtilities.invokeLater(() -> refresh(false)); } // the execution threads move the world

  /** Redraws the world, following the end of the timeline if it was shown (or if asked) */
  private void refresh(boolean toEnd)
  {
    if (world == null)
      return;
    JViewport viewport = scroll.getViewport();
    boolean atEnd      = viewport.getViewPosition().y + viewport.getHeight() >= canvas.getHeight() - 5;
    canvas.revalidate();
    canvas.repaint();
    if (timeline && (atEnd || toEnd))
      SwingUtilities.invokeLater(() -> canvas.scrollRectToVisible(new Rectangle(0, canvas.getPreferredSize().height - 1, 1, 1)));
  }

  private Link linkAt(MouseEvent event)
  {
    for (Link link : links)
      if (link.area().contains(event.getPoint()))
        return link;
    return null;
  }

  /** A state of the timeline, placed at that height */
  private record Placed(Scene scene, int top) {}

  /** The states shown by the timeline with their place: the first, the events asked, and the last one */
  private List<Placed> timelineLayout()
  {
    List<Scene> history = ((PosixWorld)world).history;
    List<Placed> res    = new ArrayList<>();
    int top             = 5;
    for (int i = 0; i < history.size(); i++) {
      Scene scene = history.get(i);
      if (i > 0 && !scene.syscall() && !allEvents && i < history.size() - 1)
        continue;
      if (i > 0)
        top += GAP;
      res.add(new Placed(scene, top));
      top += sceneHeight(scene);
    }
    return res;
  }

  /** The drawing of the world, in the scroll pane: its current state, or its timeline */
  private class Canvas extends JComponent implements Scrollable {
    private static final long serialVersionUID = 1L;

    Canvas()
    {
      setToolTipText(""); // registers the canvas to the tooltip manager, that then asks getToolTipText(MouseEvent)
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

    @Override public String getToolTipText(MouseEvent event)
    {
      for (int i = tips.size() - 1; i >= 0; i--) // the last drawn is on top, as a cell over the queue that contains it
        if (tips.get(i).area().contains(event.getPoint()))
          return tips.get(i).text();
      return null;
    }

    @Override public Dimension getPreferredSize()
    {
      if (world == null)
        return new Dimension(0, 0);
      if (!timeline)
        return new Dimension(0, 10 + sceneHeight(Scene.live((PosixWorld)world)));
      List<Placed> placed = timelineLayout();
      Placed last         = placed.get(placed.size() - 1);
      return new Dimension(0, last.top() + sceneHeight(last.scene()) + 25);
    }

    @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
    @Override public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) { return ROW; }
    @Override public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction) { return visible.height - ROW; }
    @Override public boolean getScrollableTracksViewportWidth() { return true; }
    @Override public boolean getScrollableTracksViewportHeight() { return getParent().getHeight() > getPreferredSize().height; }

    @Override public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
      g2.setColor(Color.white);
      g2.fillRect(0, 0, getWidth(), getHeight());
      tips.clear();
      links.clear();
      if (world == null)
        return;
      PosixWorld w = (PosixWorld)world;
      g2.setColor(Color.black);
      if (!timeline) {
        g2.drawString("t = " + w.now + "s", (getWidth() - 40) / 2, 18);
        scene(g2, 10, Scene.live(w), true);
        return;
      }
      Rectangle clip = g2.getClipBounds();
      int previous   = -1; // the bottom of the previous state
      for (Placed p : timelineLayout()) {
        int bottom = p.top() + sceneHeight(p.scene());
        if (previous >= 0 && p.top() >= clip.y && previous <= clip.y + clip.height)
          event(g2, previous, p.top(), p.scene());
        if (bottom >= clip.y && p.top() <= clip.y + clip.height)
          scene(g2, p.top(), p.scene(), false);
        previous = bottom;
      }
      if (w.history.size() >= PosixWorld.MAX_SCENES) {
        g2.setColor(Color.gray);
        g2.drawString(Game.i18n.tr("The timeline stops after {0} events.", PosixWorld.MAX_SCENES), 10, previous + 18);
      }
    }
  }

  /** The height of the drawing of a state: the machine of the student's process, or the ones of the peers under each other */
  private int sceneHeight(Scene scene)
  {
    int student = machineHeight(rows(scene.processes().get(0)).size(), !scene.files().isEmpty());
    int peers   = -20;
    for (PosixProcess process : scene.processes().subList(1, scene.processes().size()))
      peers += machineHeight(rows(process).size(), false) + 20;
    return 15 + Math.max(student, peers);
  }

  /** The height of a machine, whose process keeps the room of a row even without any (when the standard streams are hidden) */
  private static int machineHeight(int rows, boolean files) { return 25 + Math.max(rows, 1) * ROW + (files ? FILE_HEIGHT + 10 : 0); }

  /**
   * Draws a state of the world from top: the machine of the student's process on the left, and the ones of the peers on the
   * right, mirrored, then the connections between their ports. In the timeline, the machines that did not act are dimmed. The
   * current state links its texts to the editor.
   */
  private void scene(Graphics2D g2, int top, Scene scene, boolean live)
  {
    Map<Object, Point> ports = new HashMap<>();
    int machineWidth         = canvas.getWidth() * 45 / 100;
    int peersTop             = top + 15;
    Composite plain          = g2.getComposite();
    for (int i = 0; i < scene.processes().size(); i++) {
      if (!live && scene.actor() >= 0 && scene.actor() != i)
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, DIMMED));
      PosixProcess process = scene.processes().get(i);
      if (i == 0)
        machine(g2, 10, machineWidth, top + 15, scene.titles().get(0), process, scene.files(), false, live, ports);
      else
        peersTop = machine(g2, canvas.getWidth() - 10 - machineWidth, machineWidth, peersTop, scene.titles().get(i), process, Map.of(), true, live, ports) + 20;
      g2.setComposite(plain);
    }

    // The connections between the ports, dashed while waiting in a backlog
    g2.setColor(NETWORK);
    for (PosixProcess process : scene.processes())
      for (FileDescription o : process.fds)
        if (o instanceof Socket s) {
          if (s.remote != null)
            wire(g2, ports.get(s), ports.get(s.remote), false);
          for (Socket pending : s.backlog)
            wire(g2, ports.get(s), ports.get(pending.remote), true);
        }
  }

  /**
   * Draws the arrow of the event leading to a state of the timeline, between the bottom of the previous state and the top of this
   * one: under the machine of the process that made the call, or between the machines for the network
   */
  private void event(Graphics2D g2, int from, int to, Scene scene)
  {
    int machineWidth = canvas.getWidth() * 45 / 100;
    int x            = scene.actor() < 0 ? canvas.getWidth() / 2 : scene.actor() == 0 ? 10 + machineWidth / 3 : canvas.getWidth() - 10 - machineWidth * 2 / 3;
    g2.setColor(Color.darkGray);
    g2.drawLine(x, from + 4, x, to - 4);
    g2.fillPolygon(new int[] {x, x - 4, x + 4}, new int[] {to - 1, to - 9, to - 9}, 3);
    String label = scene.actor() == 0 ? scene.what() : scene.who() + ": " + scene.what();
    Font font    = g2.getFont();
    g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
    int y = (from + to) / 2 + 4;
    g2.setColor(Color.gray);
    g2.drawString("t=" + scene.now(), 10, y);
    g2.setColor(Color.black);
    g2.drawString(fit(g2, label, canvas.getWidth() - x - 18), x + 8, y);
    g2.setFont(font);
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
   * flows towards the network (or towards the process), its capacity (0 if unbounded) whose free places are drawn empty, and what
   * each chunk is (such as "Datagram from 10.0.0.2:3000"), shown in its tooltip, or null if the chunks need no tooltip.
   */
  private record Queue(List<String> chunks, boolean eof, boolean outgoing, int capacity, String role, List<String> labels) {
    Queue(List<String> chunks, boolean eof, boolean outgoing, int capacity, String role) { this(chunks, eof, outgoing, capacity, role, null); }
  }

  private static final String SEND    = "Send queue: the data sent by this process waits here before travelling to the peer";
  private static final String RECEIVE = "Receive queue: the data from the peer waits here until this process receives it";
  private static final String BACKLOG = "Backlog: the connected clients wait here until accept() takes them";
  /** The cell ending a chunk shortened to its share of a queue */
  private static final String ELLIPSIS = "…";
  private static final String DATAGRAMS =
      "Receive queue: each datagram waits here, whole, until this process receives it (the bytes beyond the buffer of recvfrom() are lost)";

  /** The row of a standard stream: its single queue holds what waits on the input, or what was written on the outputs */
  private static Row stream(int fd, StandardStream stream)
  {
    String output = stream.name.equals("stdout") ? "output" : "error";
    String role   = stream.input ? "Standard input: its data waits here until this process reads it"
                                 : "Standard " + output + ": everything this process wrote on it, displayed in the console";
    Queue queue   = new Queue(stream.data.isEmpty() ? List.of() : List.of(stream.data), stream.input, !stream.input, 0, role);
    return new Row(fd, new String[] {stream.name}, List.of(queue), stream.input, !stream.input, null, stream);
  }

  /** The rows of the descriptors of a process, without its standard streams if they are hidden */
  private static List<Row> rows(PosixProcess process)
  {
    List<Row> rows = new ArrayList<>();
    for (int fd = showStd ? 0 : 3; fd < process.fds.size(); fd++)
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
    if (o instanceof DatagramSocket s) { // the datagrams leave at once, so only the received ones wait, one chunk each
      List<String> senders = s.incoming.stream().map(d -> "Datagram from " + d.ip() + ":" + d.port()).toList();
      Queue received       = new Queue(s.incoming.stream().map(Datagram::data).toList(), false, false, 0, DATAGRAMS, senders);
      return new Row(fd, new String[] {"UDP socket"}, List.of(received), true, true, s.port >= 0 ? String.valueOf(s.port) : null, s);
    }
    return new Row(fd, null, false, false);
  }

  /** The queues of a connected socket: the sending one, and the receiving one, each followed by the end of the stream if needed */
  private static List<Queue> connection(List<String> sent, boolean sentEof, List<String> received, boolean receivedEof)
  {
    List<String> packets = Collections.nCopies(received.size(), "Packet");
    return List.of(new Queue(sent, sentEof, true, 0, SEND), new Queue(received, receivedEof, false, 0, RECEIVE, packets));
  }

  /**
   * Draws a machine: its process with one row per descriptor, the objects of the descriptors, and the ports on its edge (on the
   * right, or on the left if mirrored), followed by its files (name and content). Stores the position of the port of each keyed
   * row in ports, and returns the bottom of the drawing. The machine of the current state links its texts to the editor.
   */
  private int machine(Graphics2D g2, int left, int width, int top, String title, PosixProcess process, Map<String, String> files, boolean mirrored,
                      boolean live, Map<Object, Point> ports)
  {
    List<Row> rows = rows(process);
    int rowsTop    = top + 15;
    int bottom     = top + machineHeight(rows.size(), !files.isEmpty());
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
    box(g2, PROCESS, processLeft, rowsTop - 5, processWidth, Math.max(rows.size(), 1) * ROW, "Process");

    Map<String, Integer> portRows = new HashMap<>(); // the row of the first socket using each numbered port
    for (int index = 0; index < rows.size(); index++) {
      Row row = rows.get(index);
      int y   = rowCenter(rowsTop, index);
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
      Rectangle area = new Rectangle(objectLeft, y - ROW / 2 + 3, objectWidth, ROW - 6);
      if (live && row.key() instanceof Socket s && s.remote != null) {
        links.add(new Link(area, title + ", socket " + row.fd(), () -> history(s)));
        tips.add(new Tip(area, "Click to see everything that went through this socket"));
      } else if (live && row.key() instanceof DatagramSocket d && d.port >= 0) {
        links.add(new Link(area, title + ", socket " + row.fd(), () -> history(d)));
        tips.add(new Tip(area, "Click to see every datagram that went through this socket"));
      } else if (live && row.key() instanceof StandardStream stream && !mirrored && (stream == process.stdout || stream == process.stderr)) {
        links.add(new Link(area, title + ", " + stream.name, () -> written(stream.name)));
        tips.add(new Tip(area, "Click to see everything that your process wrote on it"));
      }
      for (int i = 0; i < row.queues().size(); i++) { // on the right of the title, one under the other, a lone queue under the title
        Queue q  = row.queues().get(i);
        int line = row.queues().size() == 1 ? 1 : i;
        queue(g2, objectLeft + 50, y - ROW / 2 + 6 + line * (CELL + 3), objectWidth - 55, q, q.outgoing() != mirrored);
      }
      if (row.port() == null)
        continue;
      int first = index;
      int portY = row.port().isEmpty() ? y : rowCenter(rowsTop, portRows.computeIfAbsent(row.port(), number -> first));
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
      file(g2, x, rowsTop + Math.max(rows.size(), 1) * ROW + 5, file.getKey(), file.getValue(), live);
      x += FILE_WIDTH + 10;
    }
    return bottom;
  }

  /** What the student's process wrote so far on stdout or stderr, read in the world at each refresh to follow its resets */
  private String written(String name)
  {
    PosixProcess student = ((PosixWorld)world).student;
    return PosixWorld.text((name.equals("stdout") ? student.stdout : student.stderr).data);
  }

  /** Everything that went through a connected socket: what arrived on it (received or not), and what it sent (transmitted or not) */
  private static String history(Socket s) { return PosixWorld.text("Received:\n" + s.arrived + "\n\nSent:\n" + s.remote.arrived + s.outgoing); }

  /**
   * Every datagram that went through a socket, in order: where it came from or went, then its bytes as hexdump -C shows them, 16
   * per line with their offset, and as text on the right (a dot for the bytes that are not printable).
   */
  private static String history(DatagramSocket s)
  {
    StringBuilder res = new StringBuilder();
    for (DatagramSocket.Passage passage : s.passages) {
      String data = passage.data();
      res.append(passage.what()).append(":\n");
      for (int line = 0; line < data.length(); line += 16) {
        String bytes = data.substring(line, Math.min(line + 16, data.length()));
        res.append(String.format("%04x ", line));
        for (int i = 0; i < 16; i++)
          res.append(i == 8 ? "  " : " ").append(i < bytes.length() ? String.format("%02x", (int)bytes.charAt(i)) : "  ");
        res.append("  |");
        for (char c : bytes.toCharArray())
          res.append(c >= ' ' && c < 0x7F ? c : '.');
        res.append("|\n");
      }
      res.append('\n');
    }
    return res.isEmpty() ? "No datagram went through this socket yet." : res.toString();
  }

  /** Draws a file as an icon with a folded corner: its name on top, then the first lines of its content (all of it as a tooltip) */
  private void file(Graphics2D g2, int x, int y, String name, String data, boolean live)
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
    String[] lines = data.split("\n", -1);
    int shown      = Math.min(lines.length, (FILE_HEIGHT - LINE - 4) / 11);
    for (int i = 0; i < shown; i++) {
      String line = i == shown - 1 && shown < lines.length ? "…" : PosixWorld.visible(lines[i]);
      g2.drawString(fit(g2, line, FILE_WIDTH - 6), x + 3, y + LINE + 12 + 11 * i);
    }
    g2.setFont(font);
    Rectangle area = new Rectangle(x, y, FILE_WIDTH, FILE_HEIGHT);
    String click   = live ? "<i>Click to open it in the editor</i>" : "";
    tips.add(new Tip(area, "<html>" + escape(name) + ":<br><pre>" + escape(PosixWorld.text(data)) + "</pre>" + click + "</html>"));
    if (live)
      links.add(new Link(area, name, () -> {
        String current = ((PosixWorld)world).files.get(name);
        return current == null ? "(this file does not exist anymore)" : PosixWorld.text(current);
      }));
  }

  private static String escape(String text) { return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"); }

  /** The vertical center of the row at that index among the rows drawn */
  private static int rowCenter(int top, int index) { return top + index * ROW + ROW / 2 - 5; }

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
   * The cells keep the reading order, and stick to the side where they leave, which an arrow shows. When the bytes do not fit,
   * each chunk of several gets its share of the width, ended by "…" if cut, so that their boundaries stay visible; a queue still
   * too long is cut, with the number of hidden bytes, and its full content in a tooltip. Labelled chunks show theirs as tooltip.
   */
  private void queue(Graphics2D g2, int x, int y, int width, Queue q, boolean leavesRight)
  {
    int fit              = Math.max(1, (width - 10) / CELL);
    int bytes            = q.chunks().stream().mapToInt(String::length).sum();
    int share            = q.chunks().size() > 1 && bytes > fit ? Math.max(2, fit / q.chunks().size()) : Integer.MAX_VALUE;
    List<String> cells   = new ArrayList<>();
    List<Integer> chunks = new ArrayList<>(); // the chunk of each cell of data
    for (int k = 0; k < q.chunks().size(); k++) {
      String chunk = q.chunks().get(k);
      int kept     = chunk.length() > share ? share - 1 : chunk.length();
      for (char c : chunk.substring(0, kept).toCharArray())
        cells.add(c == '\n' ? "⏎" : c == '\r' ? "↵" : c == '\t' ? "⇥" : c < ' ' || c >= 0x7F ? "·" : String.valueOf(c)); // the bytes in the tooltip
      if (kept < chunk.length())
        cells.add(ELLIPSIS);
      while (chunks.size() < cells.size())
        chunks.add(k);
    }
    int used = cells.size();
    while (cells.size() < q.capacity())
      cells.add(null); // a free place
    if (q.eof())
      cells.add("EOF");

    int hidden = 0;
    if (cells.size() > fit) {
      cells             = cells.subList(0, fit - 1);
      hidden            = bytes - (int)cells.stream().limit(used).filter(c -> c != null && !ELLIPSIS.equals(c)).count();
      StringBuilder all = new StringBuilder("<html>");
      for (int k = 0; k < q.chunks().size(); k++) { // one chunk per line if they are labelled
        if (q.labels() != null)
          all.append(k > 0 ? "<br>" : "").append(escape(q.labels().get(k))).append(": ");
        all.append(escape("'" + PosixWorld.bytes(q.chunks().get(k)) + "'"));
      }
      tips.add(new Tip(new Rectangle(x, y, width, CELL), all.append(q.eof() ? " then EOF" : "").append("</html>").toString()));
    }
    int cellsWidth = cells.isEmpty() ? CELL // the place of an empty queue
                                     : cells.stream().mapToInt(c -> "EOF".equals(c) ? 2 * CELL : CELL).sum() + (hidden > 0 ? 2 * CELL : 0);
    int cx         = leavesRight ? x + width - 10 - cellsWidth : x + 10;

    g2.setColor(HIGHLIGHT);
    arrowHead(g2, leavesRight ? x + width : x, y + CELL / 2, leavesRight ? 1 : -1);
    tips.add(new Tip(new Rectangle(leavesRight ? x + width - 9 : x - 1, y, 10, CELL), q.role()));
    Font font = g2.getFont();
    g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
    int chunkStart = cx;
    for (int i = 0; i < cells.size(); i++) {
      String cell = cells.get(i);
      int w       = "EOF".equals(cell) ? 2 * CELL : CELL;
      g2.setColor(i < used ? Color.white : KERNEL);
      g2.fillRect(cx, y, w, CELL);
      g2.setColor(Color.black);
      g2.drawRect(cx, y, w, CELL);
      if (i > 0 && i < used && !chunks.get(i).equals(chunks.get(i - 1))) {
        g2.fillRect(cx - 1, y, 3, CELL + 1); // a chunk boundary: recv() never returns more than one chunk
        chunkStart = cx;
      }
      if (q.labels() != null && i < used && (i + 1 == Math.min(used, cells.size()) || !chunks.get(i + 1).equals(chunks.get(i)))) { // the end of a chunk
        String chunk = q.chunks().get(chunks.get(i));
        tips.add(new Tip(new Rectangle(chunkStart, y, cx + w - chunkStart, CELL),
                         q.labels().get(chunks.get(i)) + ", " + chunk.length() + " bytes: '" + PosixWorld.bytes(chunk) + "'"));
      }
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
