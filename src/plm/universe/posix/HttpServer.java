package plm.universe.posix;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * An HTTP/1.1 server (RFC 9112) serving static files with GET and HEAD, exact to the byte as the usual servers. Each response
 * gives the size of its body in Content-Length, after its headers ended by an empty line. The connections are persistent (the
 * server waits for the next request on the same connection) in HTTP/1.1 unless the request says "Connection: close", and closed
 * after the response in HTTP/1.0 unless it says "Connection: keep-alive". So the clients must use Content-Length to frame the
 * body, unless they ask the server to close the connection after it.
 *
 * As most servers, it accepts lines ended by \n alone, ignores the case of the header names, and answers the errors with a small
 * HTML page: 400 for a malformed request (or an HTTP/1.1 one without Host header, after which it closes the connection), 404 for
 * a missing file, 405 for another method, 505 for another version. The Date header starts on 2024-01-01 at midnight, and follows
 * the clock of the world. The idle connections are never closed by default, so that a client waiting for the end of the stream
 * instead of reading Content-Length is reported in a deadlock (see keepAlive()).
 */
public class HttpServer extends ProgramPeer {
  public static final String SOFTWARE         = "plm-httpd/1.0";
  private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH).withZone(ZoneOffset.UTC);
  private static final Instant EPOCH          = Instant.parse("2024-01-01T00:00:00Z");

  private final int port;
  private int packetSize;
  private int keepAlive = -1;
  private int master;
  private final Map<String, String> files = new TreeMap<>();
  /** The connections, with the time of their last request (or of their opening) */
  private final Map<Integer, Integer> clients = new TreeMap<>();

  public HttpServer(String ip, int port)
  {
    super(ip, "HTTP server");
    this.port = port;
  }

  private HttpServer(HttpServer other, Map<FileDescription, FileDescription> copies)
  {
    super(other, copies);
    port       = other.port;
    packetSize = other.packetSize;
    keepAlive  = other.keepAlive;
    master     = other.master;
    files.putAll(other.files);
    clients.putAll(other.clients);
  }

  @Override protected HttpServer copy(Map<FileDescription, FileDescription> copies) { return new HttpServer(this, copies); }

  /** Serves that content at that path (such as /doc/howto.txt), as HTML if the path ends with .html and as plain text otherwise */
  public HttpServer file(String path, String content)
  {
    files.put(path, ascii(content));
    return this;
  }

  /** Cuts the responses in packets of that size, one per second */
  public HttpServer packets(int size)
  {
    packetSize = size;
    return this;
  }

  /** Closes the connections idle for that many seconds, as the real servers (such as Apache after 5 seconds) */
  public HttpServer keepAlive(int seconds)
  {
    keepAlive = seconds;
    return this;
  }

  @Override protected void start() { master = listen(port); }

  @Override protected boolean act()
  {
    int fd = accept(master);
    if (fd >= 0) {
      clients.put(fd, now());
      return true;
    }
    for (Map.Entry<Integer, Integer> client : clients.entrySet()) {
      int connection = client.getKey();
      String data    = peek(connection);
      int length     = requestLength(data);
      if (length > 0 && data.length() >= length) {
        clients.put(connection, now());
        respond(connection, receive(connection, length));
        return true;
      }
      if (ended(connection) || (keepAlive >= 0 && data.isEmpty() && now() >= client.getValue() + keepAlive)) {
        close(connection);
        clients.remove(connection);
        return true;
      }
    }
    return false;
  }

  @Override protected int timer() { return keepAlive < 0 ? -1 : clients.values().stream().mapToInt(last -> last + keepAlive).min().orElse(-1); }

  @Override protected String waiting() { return clients.isEmpty() ? "waiting for a connection" : "waiting for a request"; }

  /** The position after the empty line ending the headers of the request that starts the data, or -1 if it did not arrive yet */
  private static int headersEnd(String data)
  {
    for (int start = 0, end = data.indexOf('\n'); end >= 0; start = end + 1, end = data.indexOf('\n', start))
      if (end == start || (end == start + 1 && data.charAt(start) == '\r'))
        return end + 1;
    return -1;
  }

  /** The length of the request that starts the data, with its body (of Content-Length bytes), or -1 if its headers did not arrive */
  private static int requestLength(String data)
  {
    int end = headersEnd(data);
    if (end < 0)
      return -1;
    String length = header(data.substring(0, end), "Content-Length");
    return end + (length != null && length.matches("\\d{1,9}") ? Integer.parseInt(length) : 0);
  }

  /** The value of that header in the head of a request, or null */
  private static String header(String head, String name)
  {
    for (String line : head.split("\n"))
      if (line.regionMatches(true, 0, name + ":", 0, name.length() + 1))
        return line.substring(name.length() + 1).strip();
    return null;
  }

  private void respond(int fd, String request)
  {
    String head   = request.substring(0, headersEnd(request));
    String[] line = head.substring(0, head.indexOf('\n')).strip().split(" ", -1);
    if (line.length != 3 || !line[2].startsWith("HTTP/") || !line[1].startsWith("/") ||
        header(head, "Content-Length") != null && !header(head, "Content-Length").matches("\\d{1,9}")) {
      reply(fd, "400 Bad Request", null, false, true);
      return;
    }
    String version = line[2];
    if (!version.equals("HTTP/1.1") && !version.equals("HTTP/1.0")) {
      reply(fd, "505 HTTP Version Not Supported", null, false, true);
      return;
    }
    if (version.equals("HTTP/1.1") && header(head, "Host") == null) {
      reply(fd, "400 Bad Request", null, false, true);
      return;
    }
    String connection = header(head, "Connection");
    boolean close     = version.equals("HTTP/1.1") ? "close".equalsIgnoreCase(connection) : !"keep-alive".equalsIgnoreCase(connection);
    String method     = line[0];
    if (!method.equals("GET") && !method.equals("HEAD")) {
      reply(fd, "405 Not Allowed", null, false, close);
      return;
    }
    String path = line[1].contains("?") ? line[1].substring(0, line[1].indexOf('?')) : line[1];
    if (files.containsKey(path))
      reply(fd, "200 OK", path, method.equals("HEAD"), close);
    else
      reply(fd, "404 Not Found", null, method.equals("HEAD"), close);
  }

  /** Sends a response: the file at that path, or the page of the error. Closes the connection after it if asked. */
  private void reply(int fd, String status, String path, boolean headOnly, boolean close)
  {
    String body     = path != null ? files.get(path)
                                   : "<html>\r\n<head><title>" + status + "</title></head>\r\n<body>\r\n<center><h1>" + status + "</h1></center>\r\n<hr><center>" +
                                     SOFTWARE + "</center>\r\n</body>\r\n</html>\r\n";
    String type     = path == null || path.endsWith(".html") ? "text/html" : "text/plain";
    String response = "HTTP/1.1 " + status + "\r\n"
                      + "Server: " + SOFTWARE + "\r\n"
                      + "Date: " + DATE.format(EPOCH.plusSeconds(now())) + "\r\n"
                      + "Content-Type: " + type + "\r\n"
                      + "Content-Length: " + body.length() + "\r\n" + (status.startsWith("405") ? "Allow: GET, HEAD\r\n" : "") +
                      "Connection: " + (close ? "close" : "keep-alive") + "\r\n"
                      + "\r\n" + (headOnly ? "" : body);
    if (packetSize > 0)
      sendInPackets(fd, response, packetSize, 1);
    else
      send(fd, response);
    if (close) {
      close(fd);
      clients.remove(fd);
    }
  }
}
