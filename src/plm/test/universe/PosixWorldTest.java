package plm.test.universe;

import static plm.universe.posix.PosixWorld.AF_INET;
import static plm.universe.posix.PosixWorld.O_CREAT;
import static plm.universe.posix.PosixWorld.O_TRUNC;
import static plm.universe.posix.PosixWorld.O_WRONLY;
import static plm.universe.posix.PosixWorld.SOCK_STREAM;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.universe.posix.FileDescription;
import plm.universe.posix.IrcServer;
import plm.universe.posix.Peer;
import plm.universe.posix.PosixEntity;
import plm.universe.posix.PosixWorld;
import plm.universe.posix.ProgramPeer;

/** Scenarios of the POSIX world, played directly on the entity (no remoting). */
public class PosixWorldTest {

  private static byte[] b(String text) { return text.getBytes(StandardCharsets.ISO_8859_1); }

  private static String s(byte[] data) { return new String(data, StandardCharsets.ISO_8859_1); }

  private static PosixEntity process(PosixWorld w) { return (PosixEntity)w.getEntity(0); }

  private static PosixWorld download(Peer server) { return new PosixWorld("download").addPeer(server); }

  /** Downloads everything from 10.0.0.1:80 into out.txt, and closes everything */
  private static void downloadAll(PosixEntity p)
  {
    int sock = p.socket(AF_INET, SOCK_STREAM, 0);
    Assertions.assertEquals(0, p.connect(sock, "10.0.0.1", 80));
    int file = p.open("out.txt", O_WRONLY | O_CREAT | O_TRUNC);
    for (String data = s(p.recv(sock, 512, 0)); !data.isEmpty(); data = s(p.recv(sock, 512, 0)))
      Assertions.assertEquals(data.length(), p.write(file, b(data)));
    p.close(file);
    p.close(sock);
  }

  @Test public void testDownloadInOneChunk()
  {
    PosixWorld w  = download(Peer.server("10.0.0.1", 80, """
        accept(3)
        send(4, "HELLO")
        transmit(4, 5)
        close(4)
        """));
    PosixEntity p = process(w);
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    Assertions.assertEquals(3, sock);
    Assertions.assertEquals(0, p.connect(sock, "10.0.0.1", 80));
    Assertions.assertEquals("HELLO", s(p.recv(sock, 512, 0)));
    Assertions.assertEquals("", s(p.recv(sock, 512, 0)));
  }

  @Test public void testRecvReturnsWhatArrived()
  {
    PosixWorld w  = download(Peer.server("10.0.0.1", 80, """
        accept(3)
        sendInPackets(4, "HELLO", 3, 1)
        close(4)
        """));
    PosixEntity p = process(w);
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    p.connect(sock, "10.0.0.1", 80);
    Assertions.assertEquals("HEL", s(p.recv(sock, 512, 0))); // the rest is still in the send queue of the server
    Assertions.assertEquals("LO", s(p.recv(sock, 512, 0)));
    Assertions.assertEquals("", s(p.recv(sock, 512, 0)));
  }

  @Test public void testRecvTakesSeveralPacketsAndLimitsToTheBuffer()
  {
    PosixWorld w  = download(Peer.server("10.0.0.1", 80, """
        accept(3)
        send(4, "HELLO")
        transmit(4, 2)
        transmit(4, 3)
        close(4)
        """));
    PosixEntity p = process(w);
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    p.connect(sock, "10.0.0.1", 80);
    Assertions.assertEquals("HELL", s(p.recv(sock, 4, 0)));
    Assertions.assertEquals("O", s(p.recv(sock, 512, 0)));
  }

  @Test public void testEndOfStreamWaitsForTheData()
  {
    PosixWorld w  = download(Peer.server("10.0.0.1", 80, """
        accept(3)
        send(4, "HELLO")
        close(4)
        sleep(1)
        transmit(4, 5)
        """));
    PosixEntity p = process(w);
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    p.connect(sock, "10.0.0.1", 80);
    Assertions.assertEquals("HELLO", s(p.recv(sock, 512, 0))); // not the end of the stream, which travels after the data
    Assertions.assertEquals("", s(p.recv(sock, 512, 0)));
  }

  @Test public void testSameResultWithOtherDescriptorNumbersWins()
  {
    Peer server     = Peer.server("10.0.0.1", 80, """
        accept(3)
        sendInPackets(4, "HELLO", 3, 1)
        close(4)
        """);
    PosixWorld ref  = download(server);
    PosixWorld curr = (PosixWorld)ref.copy();
    downloadAll(process(ref));

    PosixEntity p = process(curr); // Opens the file before the socket, so the descriptor numbers differ
    int file      = p.open("out.txt", O_WRONLY | O_CREAT | O_TRUNC);
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    p.connect(sock, "10.0.0.1", 80);
    for (String data = s(p.recv(sock, 512, 0)); !data.isEmpty(); data = s(p.recv(sock, 512, 0)))
      p.write(file, b(data));
    p.close(sock);
    p.close(file);

    Assertions.assertTrue(curr.winning(ref), ref.diffTo(curr, null));
  }

  @Test public void testLeakedDescriptorLoses()
  {
    PosixWorld ref  = download(Peer.server("10.0.0.1", 80, """
        accept(3)
        send(4, "HELLO")
        transmit(4, 5)
        close(4)
        """));
    PosixWorld curr = (PosixWorld)ref.copy();
    downloadAll(process(ref));

    PosixEntity p = process(curr);
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    p.connect(sock, "10.0.0.1", 80);
    int file = p.open("out.txt", O_WRONLY | O_CREAT | O_TRUNC);
    p.write(file, p.recv(sock, 512, 0));
    p.close(file); // the socket is not closed

    Assertions.assertFalse(curr.winning(ref));
    Assertions.assertTrue(ref.diffTo(curr, null).contains("Open descriptors"), ref.diffTo(curr, null));
  }

  @Test public void testConnectionRefusedUntilTheServerAccepts()
  {
    PosixWorld w  = download(Peer.server("10.0.0.1", 80, """
        sleep(3)
        accept(3)
        send(4, "HELLO")
        transmit(4, 5)
        close(4)
        """));
    PosixEntity p = process(w);
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    Assertions.assertEquals(-1, p.connect(sock, "10.0.0.1", 80));
    Assertions.assertEquals("ECONNREFUSED", p.lastError());
    p.sleep(3);
    Assertions.assertEquals(0, p.connect(sock, "10.0.0.1", 80));
    Assertions.assertEquals("HELLO", s(p.recv(sock, 512, 0)));
  }

  @Test public void testBusyLoopIsDetected()
  {
    PosixWorld w  = download(Peer.server("10.0.0.1", 80, """
        sleep(3)
        accept(3)
        """));
    PosixEntity p = process(w);
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    Assertions.assertThrows(IllegalStateException.class, () -> {
      while (p.connect(sock, "10.0.0.1", 80) < 0) {
      }
    });
  }

  @Test public void testDeadlockIsReported()
  {
    PosixWorld w  = download(Peer.server("10.0.0.1", 80, """
        accept(3)
        recv(4, 5)
        send(4, "ANSWER")
        transmit(4, 6)
        close(4)
        """));
    PosixEntity p = process(w);
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    p.connect(sock, "10.0.0.1", 80);
    IllegalStateException e = Assertions.assertThrows(IllegalStateException.class, () -> p.recv(sock, 512, 0));
    Assertions.assertTrue(e.getMessage().contains("RECV"), e.getMessage());
  }

  @Test public void testServerSide()
  {
    PosixWorld w  = new PosixWorld("server").addPeer(Peer.client("10.0.0.2", 3000, """
        sleep(1)
        connect(3)
        send(3, "PING")
        transmit(3, 4)
        recv(3, 4)
        close(3)
        """));
    PosixEntity p = process(w);
    int master    = p.socket(AF_INET, SOCK_STREAM, 0);
    Assertions.assertEquals(0, p.bind(master, "0.0.0.0", 3000));
    Assertions.assertEquals(0, p.listen(master, 5));
    int client = p.accept(master);
    Assertions.assertEquals(4, client);
    Assertions.assertEquals("PING", s(p.recv(client, 512, 0)));
    Assertions.assertEquals(4, p.send(client, b("PONG"), 0));
    Assertions.assertEquals("", s(p.recv(client, 512, 0)));
    Assertions.assertEquals(0, p.close(client));
    Assertions.assertEquals(0, p.close(master));
  }

  /** Starts a server on port 3000 and accepts the connection of a client peer that sends PING and expects it back */
  private static PosixWorld acceptedPing()
  {
    PosixWorld w  = new PosixWorld("ping").addPeer(Peer.client("10.0.0.2", 3000, """
        sleep(1)
        connect(3)
        send(3, "PING")
        transmit(3, 4)
        recv(3, 4)
        close(3)
        """));
    PosixEntity p = process(w);
    int master    = p.socket(AF_INET, SOCK_STREAM, 0);
    p.bind(master, "0.0.0.0", 3000);
    p.listen(master, 5);
    Assertions.assertEquals(4, p.accept(master));
    return w;
  }

  /** Echoes the PING of the client on descriptor 4 (sending it n times), and closes everything */
  private static void echo(PosixEntity p, int n)
  {
    byte[] data = p.recv(4, 512, 0);
    for (int i = 0; i < n; i++)
      p.send(4, data, 0);
    p.close(4);
    p.close(3);
  }

  @Test public void testCopyKeepsTheConnections()
  {
    PosixWorld w    = acceptedPing();
    PosixWorld copy = (PosixWorld)w.copy(); // in the middle of the connection, that the copy must keep between its processes
    echo(process(w), 1);
    echo(process(copy), 1);
    Assertions.assertTrue(copy.winning(w), w.diffTo(copy, null));
  }

  @Test public void testExtraDataSentToThePeerLoses()
  {
    PosixWorld ref  = acceptedPing();
    PosixWorld curr = (PosixWorld)ref.copy();
    echo(process(ref), 1);
    echo(process(curr), 2); // the client only reads the first PING, but the second one arrived too
    Assertions.assertFalse(curr.winning(ref));
    Assertions.assertTrue(ref.diffTo(curr, null).contains("received 'PINGPING'"), ref.diffTo(curr, null));
  }

  @Test public void testClientConnectingBeforeListenIsRefused()
  {
    PosixWorld w  = new PosixWorld("early").addPeer(Peer.client("10.0.0.2", 3000, """
        connect(3)
        send(3, "PING")
        transmit(3, 4)
        """));
    PosixEntity p = process(w);
    int master    = p.socket(AF_INET, SOCK_STREAM, 0); // the peer connects (and is refused) at the first system call
    p.bind(master, "0.0.0.0", 3000);
    p.listen(master, 5);
    Assertions.assertThrows(IllegalStateException.class, () -> p.accept(master));
  }

  @Test public void testStandardOutputCounts()
  {
    PosixWorld ref  = new PosixWorld("stdout");
    PosixWorld curr = (PosixWorld)ref.copy();
    Assertions.assertEquals(6, process(ref).write(1, b("Hello\n")));

    Assertions.assertFalse(curr.winning(ref)); // nothing written
    Assertions.assertTrue(ref.diffTo(curr, null).contains("standard output"), ref.diffTo(curr, null));
    process(curr).write(1, b("Hello\n"));
    Assertions.assertTrue(curr.winning(ref), ref.diffTo(curr, null));
  }

  @Test public void testStandardInput()
  {
    PosixEntity p = process(new PosixWorld("stdin").setStdin("ab"));
    Assertions.assertEquals("a", s(p.read(0, 1)));
    Assertions.assertEquals("b", s(p.read(0, 10)));
    Assertions.assertEquals("", s(p.read(0, 10)));
    Assertions.assertEquals(-1, p.write(0, b("x")));
    Assertions.assertEquals("EBADF", p.lastError());
  }

  @Test public void testScriptErrorsGiveTheirLine()
  {
    IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> Peer.server("10.0.0.1", 80, """
        accept(3) // comments are ignored
        sned(4, "HELLO")
        """));
    Assertions.assertTrue(e.getMessage().startsWith("Line 2 of the script of the server peer: unknown call sned()"), e.getMessage());

    e = Assertions.assertThrows(IllegalArgumentException.class, () -> Peer.server("10.0.0.1", 80, "accept(4)"));
    Assertions.assertTrue(e.getMessage().contains("Descriptor 4 is not the listening socket"), e.getMessage());

    e = Assertions.assertThrows(IllegalArgumentException.class, () -> Peer.server("10.0.0.1", 80, "accept(3) close(4) send(4, \"HELLO\")"));
    Assertions.assertTrue(e.getMessage().startsWith("Line 1") && e.getMessage().contains("Descriptor 4 is not a connection"), e.getMessage());

    e = Assertions.assertThrows(IllegalArgumentException.class, () -> Peer.server("10.0.0.1", 80, "send(4, \"HELLO)"));
    Assertions.assertTrue(e.getMessage().contains("unterminated"), e.getMessage());
  }

  @Test public void testServerPeerWithSeveralConnections()
  {
    PosixWorld w  = new PosixWorld("two connections").addPeer(Peer.server("10.0.0.1", 80, """
        accept(3)
        accept(3)
        send(5, "second")
        transmit(5, 6)
        send(4, "first")
        transmit(4, 5)
        close(4)
        close(5)
        """));
    PosixEntity p = process(w);
    int first     = p.socket(AF_INET, SOCK_STREAM, 0);
    Assertions.assertEquals(0, p.connect(first, "10.0.0.1", 80));
    int second = p.socket(AF_INET, SOCK_STREAM, 0);
    Assertions.assertEquals(0, p.connect(second, "10.0.0.1", 80));
    Assertions.assertEquals("first", s(p.recv(first, 10, 0)));
    Assertions.assertEquals("second", s(p.recv(second, 10, 0)));
    Assertions.assertEquals("", s(p.recv(first, 10, 0)));
    p.close(first);
    p.close(second);
  }

  @Test public void testClientPeerWithSeveralConnections()
  {
    PosixWorld w  = new PosixWorld("two connections").addPeer(Peer.client("10.0.0.2", 3000, """
        sleep(1)
        connect(3)
        socket()
        connect(4)
        send(4, "B")
        transmit(4, 1)
        close(3)
        close(4)
        """));
    PosixEntity p = process(w);
    int master    = p.socket(AF_INET, SOCK_STREAM, 0);
    p.bind(master, "0.0.0.0", 3000);
    p.listen(master, 5);
    int a = p.accept(master);
    int b = p.accept(master);
    Assertions.assertEquals("B", s(p.recv(b, 10, 0)));
    Assertions.assertEquals("", s(p.recv(a, 10, 0)));
    p.close(a);
    p.close(b);
    p.close(master);
  }

  /** A server of the tests on port 7, answering each line ended by \r\n in upper case, in packets of 2 bytes, and closing on QUIT */
  private static final class ShoutServer extends ProgramPeer {
    private int master;
    private final List<Integer> clients = new ArrayList<>();

    ShoutServer() { super("10.0.0.1", "shout server"); }

    private ShoutServer(ShoutServer other, Map<FileDescription, FileDescription> copies)
    {
      super(other, copies);
      master = other.master;
      clients.addAll(other.clients);
    }

    @Override protected ShoutServer copy(Map<FileDescription, FileDescription> copies) { return new ShoutServer(this, copies); }

    @Override protected void start() { master = listen(7); }

    @Override protected boolean act()
    {
      int client = accept(master);
      if (client >= 0)
        return clients.add(client);
      for (int fd : clients) {
        String line = receive(fd, "\r\n");
        if ("QUIT".equals(line) || (line == null && ended(fd))) {
          close(fd);
          return clients.remove((Integer)fd);
        }
        if (line != null) {
          sendInPackets(fd, line.toUpperCase() + "\r\n", 2, 1);
          return true;
        }
      }
      return false;
    }

    @Override protected String waiting() { return "waiting for a line"; }
  }

  /** Receives on that descriptor until the end of a line */
  private static String line(PosixEntity p, int fd)
  {
    StringBuilder res = new StringBuilder();
    while (!res.toString().endsWith("\r\n"))
      res.append(s(p.recv(fd, 100, 0)));
    return res.toString();
  }

  @Test public void testProgramPeerServesSeveralClients()
  {
    PosixWorld w  = new PosixWorld("shout").addPeer(new ShoutServer());
    PosixEntity p = process(w);
    int a         = p.socket(AF_INET, SOCK_STREAM, 0);
    Assertions.assertEquals(0, p.connect(a, "10.0.0.1", 7));
    int b = p.socket(AF_INET, SOCK_STREAM, 0);
    Assertions.assertEquals(0, p.connect(b, "10.0.0.1", 7));
    p.send(a, b("hel"), 0); // a line in two parts
    p.send(a, b("lo\r\n"), 0);
    p.send(b, b("QUIT\r\n"), 0);
    Assertions.assertEquals("", s(p.recv(b, 10, 0)));
    Assertions.assertEquals("HE", s(p.recv(a, 10, 0))); // the first packet only

    PosixWorld copy = (PosixWorld)w.copy(); // in the middle of the answer, whose packets the copy must keep
    Assertions.assertEquals("LLO\r\n", line(p, a));
    Assertions.assertEquals("LLO\r\n", line(process(copy), a));
    p.close(a);
    p.close(b);
  }

  /** Reads the lines (ended by \r\n) of a connection, whatever the packets carrying them */
  private static final class Lines {
    private final PosixEntity p;
    private final int fd;
    private final StringBuilder pending = new StringBuilder();

    Lines(PosixEntity p, int fd)
    {
      this.p  = p;
      this.fd = fd;
    }

    /** The next line, without its \r\n */
    String next()
    {
      while (pending.indexOf("\r\n") < 0)
        pending.append(s(p.recv(fd, 512, 0)));
      String res = pending.substring(0, pending.indexOf("\r\n"));
      pending.delete(0, res.length() + 2);
      return res;
    }

    /** Skips the lines until one starting with that text, and returns it */
    String until(String start)
    {
      String line = next();
      while (!line.startsWith(start))
        line = next();
      return line;
    }
  }

  /** A world with an IRC server on 10.0.0.1:6667 where Bob answers a precise question, sending in packets of that size if positive */
  private static PosixWorld irc(int packetSize)
  {
    return new PosixWorld("irc").addPeer(new IrcServer("10.0.0.1", 6667)
                                             .channel("#welcome", "Welcome, little buggles!", "Alice", "Bob")
                                             .say(2, "#welcome", "Alice", "Has anyone seen my brush? I left it near the wall.")
                                             .answer("Bob", "Hi Bob!", "Hi! The doc is /doc.txt", "Sorry, I did not get it")
                                             .packets(packetSize));
  }

  /** Registers as student, and joins #welcome */
  private static Lines joined(PosixEntity p)
  {
    int sock = p.socket(AF_INET, SOCK_STREAM, 0);
    Assertions.assertEquals(0, p.connect(sock, "10.0.0.1", 6667));
    Lines lines = new Lines(p, sock);
    p.send(sock, b("NICK student\r\nUSER student 0 * :A student\r\n"), 0);
    Assertions.assertEquals(":irc.buggleinc.com 001 student :Welcome to the BuggleInc IRC Network student!student@10.0.0.10", lines.next());
    lines.until(":irc.buggleinc.com 376 ");
    p.send(sock, b("JOIN #welcome\r\n"), 0);
    Assertions.assertEquals(":student!student@10.0.0.10 JOIN #welcome", lines.next());
    Assertions.assertEquals(":irc.buggleinc.com 332 student #welcome :Welcome, little buggles!", lines.next());
    Assertions.assertEquals(":irc.buggleinc.com 353 student = #welcome :Alice Bob student", lines.next());
    Assertions.assertEquals(":irc.buggleinc.com 366 student #welcome :End of /NAMES list.", lines.next());
    return lines;
  }

  @Test public void testIrcServer()
  {
    for (int packetSize : new int[] {0, 7}) {
      PosixEntity p = process(irc(packetSize));
      Lines lines   = joined(p);
      p.send(3, b("PRIVMSG Bob :Hello\r\n"), 0);
      Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG student :Sorry, I did not get it", lines.next());
      p.send(3, b("privmsg Bob :Hi Bob!\n"), 0); // case-insensitive commands, and a bare \n
      if (packetSize == 0)                       // at t=2, Alice speaks before Bob answers (the packets are so slow that she spoke before the JOIN)
        Assertions.assertEquals(":Alice!alice@buggleinc.com PRIVMSG #welcome :Has anyone seen my brush? I left it near the wall.", lines.next());
      Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG student :Hi! The doc is /doc.txt", lines.next());
      p.send(3, b("QUIT\r\n"), 0);
      Assertions.assertEquals("ERROR :Closing Link: 10.0.0.10 (Client Quit)", lines.next());
      Assertions.assertEquals("", s(p.recv(3, 10, 0)));
      p.close(3);
    }
  }

  @Test public void testIrcServerErrors()
  {
    PosixEntity p = process(irc(0));
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    p.connect(sock, "10.0.0.1", 6667);
    Lines lines = new Lines(p, sock);
    p.send(sock, b("JOIN #welcome\r\n"), 0);
    Assertions.assertEquals(":irc.buggleinc.com 451 * :You have not registered", lines.next());
    p.send(sock, b("NICK Bob\r\n"), 0);
    Assertions.assertEquals(":irc.buggleinc.com 433 * Bob :Nickname is already in use", lines.next());
    p.send(sock, b("NICK student\r\nUSER student\r\n"), 0);
    Assertions.assertEquals(":irc.buggleinc.com 461 student USER :Not enough parameters", lines.next());
    p.send(sock, b("USER student 0 * :A student\r\nHELLO\r\n"), 0);
    Assertions.assertEquals(":irc.buggleinc.com 421 student HELLO :Unknown command", lines.until(":irc.buggleinc.com 421"));
    p.send(sock, b("PRIVMSG Carol :Hi\r\n"), 0);
    Assertions.assertEquals(":irc.buggleinc.com 401 student Carol :No such nick/channel", lines.next());
    p.close(sock);
  }

  @Test public void testUnknownFileAndBadDescriptor()
  {
    PosixEntity p = process(new PosixWorld("errors"));
    Assertions.assertEquals(-1, p.open("missing.txt", 0));
    Assertions.assertEquals("ENOENT", p.lastError());
    Assertions.assertEquals(-1, p.close(42));
    Assertions.assertEquals("EBADF", p.lastError());
    Assertions.assertNull(p.recv(1, 10, 0));
    Assertions.assertEquals("ENOTSOCK", p.lastError());
  }
}
