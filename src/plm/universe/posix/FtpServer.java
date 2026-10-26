package plm.universe.posix;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * An FTP server (RFC 959) in passive mode, exact to the byte as vsftpd: the clients log in anonymously (USER anonymous, then PASS
 * with anything), and each transfer (RETR of a file, or LIST of a directory) goes through a new data connection, which the client
 * opens to the port given by the reply to PASV (227, whose last two numbers p1,p2 give the port p1*256+p2) or to EPSV (229). The
 * server sends the data on it and closes it at the end, before confirming on the control connection (226). The other commands
 * are SYST, TYPE, PWD, CWD and QUIT, and the errors are the usual replies (500 unknown command, 530 not logged in, 550 missing
 * file or directory, 425 no data connection...).
 *
 * The replies are lines ended by \r\n, made of a code and a text. A reply may span several lines: its first lines start with the
 * code followed by a dash (220-), and its last one with the code followed by a space (220 ), as the welcome of this server. As
 * most servers, it splits the commands on \n and ignores a \r before it, and the commands are case-insensitive.
 */
public class FtpServer extends ProgramPeer {
  public static final String SOFTWARE = "plm-ftpd 1.0";
  /** The first port of the data connections */
  private static final int DATA_PORTS = 40000;

  /** A control connection, with the state of its session */
  private static final class Session {
    boolean userGiven, loggedIn, binary;
    String cwd = "/";
    /** The listening socket of the next data connection, and the data connection once accepted, or -1 */
    int dataListen = -1, data = -1;
    /** The transfer waiting for the data connection (LIST or RETR), and its argument */
    String transfer, argument;

    Session() {}

    Session(Session other)
    {
      userGiven  = other.userGiven;
      loggedIn   = other.loggedIn;
      binary     = other.binary;
      cwd        = other.cwd;
      dataListen = other.dataListen;
      data       = other.data;
      transfer   = other.transfer;
      argument   = other.argument;
    }
  }

  private final int port;
  private int packetSize;
  private int master;
  private int nextDataPort                     = DATA_PORTS;
  private final List<String> welcome           = new ArrayList<>(List.of("Welcome to the BuggleInc FTP server."));
  private final Map<String, String> files      = new TreeMap<>();
  private final Map<Integer, Session> sessions = new TreeMap<>();

  public FtpServer(String ip, int port)
  {
    super(ip, "FTP server");
    this.port = port;
  }

  private FtpServer(FtpServer other, Map<FileDescription, FileDescription> copies)
  {
    super(other, copies);
    port         = other.port;
    packetSize   = other.packetSize;
    master       = other.master;
    nextDataPort = other.nextDataPort;
    welcome.clear();
    welcome.addAll(other.welcome);
    files.putAll(other.files);
    other.sessions.forEach((fd, session) -> sessions.put(fd, new Session(session)));
  }

  @Override protected FtpServer copy(Map<FileDescription, FileDescription> copies) { return new FtpServer(this, copies); }

  // ---------------------------------------------------------------- the scenario, given by the exercise

  /** Serves that content at that absolute path (such as /pub/file.txt), whose directories exist then */
  public FtpServer file(String path, String content)
  {
    files.put(path, ascii(content));
    return this;
  }

  /** Replaces the lines of the welcome message, before its last line "220 plm-ftpd 1.0 ready." */
  public FtpServer welcome(String... lines)
  {
    welcome.clear();
    for (String line : lines)
      welcome.add(ascii(line));
    return this;
  }

  /** Cuts everything the server sends (replies and data) in packets of that size, one per second */
  public FtpServer packets(int size)
  {
    packetSize = size;
    return this;
  }

  // ---------------------------------------------------------------- the event loop

  @Override protected void start() { master = listen(port); }

  @Override protected boolean act()
  {
    int fd = accept(master);
    if (fd >= 0) {
      sessions.put(fd, new Session());
      StringBuilder lines = new StringBuilder();
      for (String line : welcome)
        lines.append("220-").append(line).append("\r\n");
      out(fd, lines + "220 " + SOFTWARE + " ready.\r\n");
      return true;
    }
    for (Map.Entry<Integer, Session> entry : sessions.entrySet()) {
      Session session = entry.getValue();
      if (session.dataListen >= 0 && session.data < 0 && (session.data = accept(session.dataListen)) >= 0) {
        if (session.transfer != null)
          transfer(entry.getKey(), session);
        return true;
      }
    }
    for (Map.Entry<Integer, Session> entry : sessions.entrySet()) {
      int control = entry.getKey();
      String line = receive(control, "\n");
      if (line != null) {
        handle(control, entry.getValue(), line.endsWith("\r") ? line.substring(0, line.length() - 1) : line);
        return true;
      }
      if (ended(control)) { // the client left without QUIT
        end(control, entry.getValue());
        return true;
      }
    }
    return false;
  }

  @Override protected String waiting() { return sessions.isEmpty() ? "waiting for a connection" : "waiting for a command"; }

  /** Sends some data, cut in packets if asked */
  private void out(int fd, String data)
  {
    if (packetSize > 0)
      sendInPackets(fd, data, packetSize, 1);
    else
      send(fd, data);
  }

  /** Sends a reply of one line on the control connection */
  private void reply(int fd, String line) { out(fd, line + "\r\n"); }

  /** Closes a session, with its data connection */
  private void end(int control, Session session)
  {
    closeData(session);
    close(control);
    sessions.remove(control);
  }

  private void closeData(Session session)
  {
    if (session.data >= 0)
      close(session.data);
    if (session.dataListen >= 0)
      close(session.dataListen);
    session.data = session.dataListen = -1;
  }

  // ---------------------------------------------------------------- the commands

  private void handle(int fd, Session session, String line)
  {
    line = line.strip();
    if (line.isEmpty())
      return;
    int space       = line.indexOf(' ');
    String command  = (space < 0 ? line : line.substring(0, space)).toUpperCase(Locale.ROOT);
    String argument = space < 0 ? "" : line.substring(space + 1).strip();
    switch (command) {
      case "USER" -> {
        if (session.loggedIn)
          reply(fd, "530 Can't change from guest user.");
        else if (!argument.equalsIgnoreCase("anonymous") && !argument.equalsIgnoreCase("ftp"))
          reply(fd, "530 This FTP server is anonymous only.");
        else {
          session.userGiven = true;
          reply(fd, "331 Please specify the password.");
        }
      }
      case "PASS" -> {
        if (session.loggedIn)
          reply(fd, "230 Already logged in.");
        else if (!session.userGiven)
          reply(fd, "503 Login with USER first.");
        else {
          session.loggedIn = true;
          reply(fd, "230 Login successful.");
        }
      }
      case "QUIT" -> {
        reply(fd, "221 Goodbye.");
        end(fd, session);
      }
      default -> {
        if (!session.loggedIn)
          reply(fd, "530 Please login with USER and PASS.");
        else
          loggedIn(fd, session, command, argument);
      }
    }
  }

  private void loggedIn(int fd, Session session, String command, String argument)
  {
    switch (command) {
      case "SYST" -> reply(fd, "215 UNIX Type: L8");
      case "TYPE" -> {
        if (argument.equalsIgnoreCase("I") || argument.equalsIgnoreCase("L 8")) {
          session.binary = true;
          reply(fd, "200 Switching to Binary mode.");
        } else if (argument.equalsIgnoreCase("A") || argument.equalsIgnoreCase("A N")) {
          session.binary = false;
          reply(fd, "200 Switching to ASCII mode.");
        } else
          reply(fd, "500 Unrecognised TYPE command.");
      }
      case "PWD" -> reply(fd, "257 \"" + session.cwd + "\" is the current directory");
      case "CWD" -> {
        String dir = resolve(session, argument);
        if (directories().contains(dir)) {
          session.cwd = dir;
          reply(fd, "250 Directory successfully changed.");
        } else
          reply(fd, "550 Failed to change directory.");
      }
      case "PASV", "EPSV" -> {
        closeData(session); // a previous data connection is replaced
        int dataPort       = nextDataPort++;
        session.dataListen = listen(dataPort);
        if (command.equals("PASV"))
          reply(fd, "227 Entering Passive Mode (" + process.ip.replace('.', ',') + "," + dataPort / 256 + "," + dataPort % 256 + ").");
        else
          reply(fd, "229 Entering Extended Passive Mode (|||" + dataPort + "|)");
      }
      case "RETR", "LIST" -> {
        if (session.dataListen < 0)
          reply(fd, "425 Use PORT or PASV first.");
        else if (command.equals("RETR") && !files.containsKey(resolve(session, argument)))
          reply(fd, "550 Failed to open file.");
        else {
          session.transfer = command;
          session.argument = argument;
          if (session.data >= 0) // the client connected before asking
            transfer(fd, session);
        }
      }
      default -> reply(fd, "500 Unknown command.");
    }
  }

  /** Sends the data of the waiting transfer on the data connection, closes it, and confirms on the control connection */
  private void transfer(int fd, Session session)
  {
    if (session.transfer.equals("RETR")) {
      String content = files.get(resolve(session, session.argument));
      reply(fd,
            "150 Opening " + (session.binary ? "BINARY" : "ASCII") + " mode data connection for " + session.argument + " (" + content.length() + " bytes).");
      if (!content.isEmpty())
        out(session.data, content);
      closeData(session);
      reply(fd, "226 Transfer complete.");
    } else {
      reply(fd, "150 Here comes the directory listing.");
      String listing = listing(resolve(session, session.argument.isEmpty() || session.argument.startsWith("-") ? "." : session.argument));
      if (!listing.isEmpty())
        out(session.data, listing);
      closeData(session);
      reply(fd, "226 Directory send OK.");
    }
    session.transfer = session.argument = null;
  }

  /** The absolute path of a path given by the client, relative to its current directory unless it starts with / */
  private static String resolve(Session session, String path)
  {
    List<String> parts = new ArrayList<>();
    for (String part : ((path.startsWith("/") ? "" : session.cwd + "/") + path).split("/"))
      if (part.equals("..")) {
        if (!parts.isEmpty())
          parts.remove(parts.size() - 1);
      } else if (!part.isEmpty() && !part.equals("."))
        parts.add(part);
    return "/" + String.join("/", parts);
  }

  /** The directories of the files, from the root */
  private TreeSet<String> directories()
  {
    TreeSet<String> res = new TreeSet<>(List.of("/"));
    for (String path : files.keySet())
      for (int slash = path.indexOf('/', 1); slash > 0; slash = path.indexOf('/', slash + 1))
        res.add(path.substring(0, slash));
    return res;
  }

  /** The content of a directory in the format of ls -l, as vsftpd sends it, or the line of a file */
  private String listing(String dir)
  {
    if (files.containsKey(dir))
      return entry('-', files.get(dir).length(), dir.substring(dir.lastIndexOf('/') + 1));
    StringBuilder sb = new StringBuilder();
    String prefix    = dir.equals("/") ? "/" : dir + "/";
    for (String sub : directories())
      if (sub.startsWith(prefix) && sub.length() > prefix.length() && sub.indexOf('/', prefix.length()) < 0)
        sb.append(entry('d', 4096, sub.substring(prefix.length())));
    files.forEach((path, content) -> {
      if (path.startsWith(prefix) && path.indexOf('/', prefix.length()) < 0)
        sb.append(entry('-', content.length(), path.substring(prefix.length())));
    });
    return sb.toString();
  }

  private static String entry(char type, int size, String name)
  {
    String mode = type == 'd' ? "drwxr-xr-x" : "-rw-r--r--";
    return String.format(Locale.ROOT, "%s    1 ftp      ftp      %8d Jan 01  2024 %s\r\n", mode, size, name);
  }

  @Override String describe()
  {
    StringBuilder sb = new StringBuilder(super.describe());
    sessions.values().forEach(session
                              -> sb.append(", session ")
                                     .append(session.loggedIn ? "logged in" : "not logged in")
                                     .append(session.transfer == null ? "" : " waiting for the data connection of " + session.transfer));
    return sb.toString();
  }
}
