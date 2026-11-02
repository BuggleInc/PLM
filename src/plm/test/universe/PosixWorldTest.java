package plm.test.universe;

import static plm.universe.posix.PosixWorld.AF_INET;
import static plm.universe.posix.PosixWorld.O_CREAT;
import static plm.universe.posix.PosixWorld.O_TRUNC;
import static plm.universe.posix.PosixWorld.O_WRONLY;
import static plm.universe.posix.PosixWorld.SOCK_DGRAM;
import static plm.universe.posix.PosixWorld.SOCK_STREAM;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lessons.network.bsdsocks.UdpEcho;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import plm.universe.ExplainedException;
import plm.universe.posix.Datagram;
import plm.universe.posix.DnsServer;
import plm.universe.posix.EchoClient;
import plm.universe.posix.FileDescription;
import plm.universe.posix.FtpServer;
import plm.universe.posix.HttpServer;
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
    Assertions.assertThrows(ExplainedException.class, () -> {
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
    ExplainedException e = Assertions.assertThrows(ExplainedException.class, () -> p.recv(sock, 512, 0));
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

  @Test public void testBinaryDataIsKeptAndShownInOctal()
  {
    PosixWorld ref  = acceptedPing();
    PosixWorld curr = (PosixWorld)ref.copy();
    process(ref).write(1, "café\n".getBytes(StandardCharsets.UTF_8));
    process(curr).write(1, "cafe\n".getBytes(StandardCharsets.UTF_8));
    Assertions.assertTrue(ref.diffTo(curr, null).contains("instead of 'café\\n'"), ref.diffTo(curr, null)); // decoded from UTF-8

    int file = process(ref).open("data.bin", O_WRONLY | O_CREAT | O_TRUNC);
    process(ref).write(file, new byte[] {0, 1, -1, '7'});
    process(ref).send(4, new byte[] {0, -1, '7'}, 0); // on the connection accepted by acceptedPing()
    curr = (PosixWorld)ref.copy();
    Assertions.assertTrue(curr.winning(ref), ref.diffTo(curr, null)); // the bytes are kept as they are
    process(curr).send(4, new byte[] {1}, 0);
    Assertions.assertTrue(ref.diffTo(curr, null).contains("received '\\0\\3777\\1'"), ref.diffTo(curr, null)); // as strace shows them
  }

  @Test public void testDatagramsArriveWholeAndAreTruncated()
  {
    PosixEntity p = process(new PosixWorld("udp"));
    int server    = p.socket(AF_INET, SOCK_DGRAM, 0);
    Assertions.assertEquals(0, p.bind(server, "0.0.0.0", 5000));
    int client = p.socket(AF_INET, SOCK_DGRAM, 0);
    Assertions.assertEquals(5, p.sendto(client, b("Hello"), 0, PosixWorld.STUDENT_IP, 5000));
    Assertions.assertEquals(5, p.sendto(client, b("World"), 0, PosixWorld.STUDENT_IP, 5000));
    Assertions.assertEquals("Hel", s(p.recvfrom(server, 3, 0)));                 // the rest of the datagram is lost
    Assertions.assertEquals(PosixWorld.STUDENT_IP + ":57346", p.lastSender()); // the ephemeral port bound by sendto()
    Assertions.assertEquals("World", s(p.recv(server, 512, 0)));
    Assertions.assertThrows(ExplainedException.class, () -> p.recvfrom(server, 512, 0)); // a deadlock
  }

  @Test public void testDatagramsWithoutReceiverAreLost()
  {
    PosixEntity p = process(new PosixWorld("udp"));
    int server    = p.socket(AF_INET, SOCK_DGRAM, 0);
    p.bind(server, "0.0.0.0", 5000);
    Assertions.assertEquals(0, p.connect(server, PosixWorld.STUDENT_IP, 6000)); // only receives from that port now
    int client = p.socket(AF_INET, SOCK_DGRAM, 0);
    Assertions.assertEquals(-1, p.send(client, b("PING"), 0));
    Assertions.assertEquals("EDESTADDRREQ", p.lastError());
    Assertions.assertEquals(4, p.sendto(client, b("PING"), 0, PosixWorld.STUDENT_IP, 5000)); // lost, as from another port
    Assertions.assertEquals(4, p.sendto(client, b("PING"), 0, "10.0.0.1", 53));              // lost, as nobody listens there
    Assertions.assertThrows(ExplainedException.class, () -> p.recvfrom(server, 512, 0));
    Assertions.assertEquals(-1, p.listen(client, 5));
    Assertions.assertEquals("EOPNOTSUPP", p.lastError());
  }

  @Test public void testScriptedUdpServerAnswersTheSender()
  {
    String script      = """
        recvfrom(3) 
        sendto(3, "Sunny")
        close(3) """;
    PosixWorld ref     = new PosixWorld("udp server").addPeer(Peer.udpServer("10.0.0.1", 9001, script));
    PosixWorld curr    = (PosixWorld)ref.copy();
    PosixEntity p      = process(ref);
    int sock           = p.socket(AF_INET, SOCK_DGRAM, 0);
    Assertions.assertEquals(8, p.sendto(sock, b("weather?"), 0, "10.0.0.1", 9001));
    Assertions.assertEquals("Sunny", s(p.recvfrom(sock, 512, 0)));
    Assertions.assertEquals("10.0.0.1:9001", p.lastSender());
    p.close(sock);

    PosixEntity other = process(curr);
    int mine          = other.socket(AF_INET, SOCK_DGRAM, 0);
    other.sendto(mine, b("weather"), 0, "10.0.0.1", 9001);
    Assertions.assertEquals("Sunny", s(other.recvfrom(mine, 512, 0)));
    other.close(mine);
    Assertions.assertTrue(ref.diffTo(curr, null).contains("received 'weather'"), ref.diffTo(curr, null)); // what the peer received
  }

  @Test public void testScriptedUdpClientWaitsForTheAnswer()
  {
    String script     = """
        sleep(1) 
        sendto(3, "Hello?") 
        recvfrom(3) 
        close(3) """;
    PosixEntity p     = process(new PosixWorld("udp client").addPeer(Peer.udpClient("10.0.0.2", 3000, script)));
    int sock          = p.socket(AF_INET, SOCK_DGRAM, 0);
    p.bind(sock, "0.0.0.0", 3000);
    Assertions.assertEquals("Hello?", s(p.recvfrom(sock, 512, 0)));
    String[] sender = p.lastSender().split(":");
    Assertions.assertEquals("10.0.0.2", sender[0]);
    Assertions.assertEquals(7, p.sendto(sock, b("Welcome"), 0, sender[0], Integer.parseInt(sender[1])));
    p.close(sock);

    PosixEntity q = process(new PosixWorld("udp client").addPeer(Peer.udpClient("10.0.0.2", 3000, script)));
    int other     = q.socket(AF_INET, SOCK_DGRAM, 0);
    q.bind(other, "0.0.0.0", 3000);
    q.recvfrom(other, 512, 0);
    q.sendto(other, b("Welcome"), 0, "10.0.0.2", 3000);                                 // to the port of the server instead of the client's one: lost
    Assertions.assertThrows(ExplainedException.class, () -> q.recvfrom(other, 512, 0)); // both wait forever
  }

  /** Two UDP clients on their own machines, sending at the same time to the port 3000 of the student */
  private static PosixWorld twoUdpClients()
  {
    String script = """
        sleep(1)
        sendto(3, "Hello?")
        recvfrom(3)
        close(3)
        """;
    return new PosixWorld("udp clients").addPeer(Peer.udpClient("10.0.0.2", 3000, script)).addPeer(Peer.udpClient("10.0.0.3", 3000, script));
  }

  @Test public void testUdpServerAnswersEachSender()
  {
    PosixWorld ref  = twoUdpClients();
    PosixWorld curr = (PosixWorld)ref.copy();
    PosixEntity p   = process(ref);
    int sock        = p.socket(AF_INET, SOCK_DGRAM, 0);
    p.bind(sock, "0.0.0.0", 3000);
    List<String> senders = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      Assertions.assertEquals("Hello?", s(p.recvfrom(sock, 512, 0)));
      senders.add(p.lastSender());
      String[] sender = p.lastSender().split(":");
      p.sendto(sock, b("Welcome"), 0, sender[0], Integer.parseInt(sender[1]));
    }
    p.close(sock);
    Assertions.assertTrue(senders.get(0).startsWith("10.0.0.2:"), senders.toString()); // the peers act in their order
    Assertions.assertTrue(senders.get(1).startsWith("10.0.0.3:"), senders.toString());
    Assertions.assertNotEquals(senders.get(0).split(":")[1], senders.get(1).split(":")[1]); // their ephemeral ports differ

    // Answering both to the address of the first client: the second one never gets its answer
    PosixEntity q = process(curr);
    int other     = q.socket(AF_INET, SOCK_DGRAM, 0);
    q.bind(other, "0.0.0.0", 3000);
    String[] first = senders.get(0).split(":");
    for (int i = 0; i < 2; i++) {
      q.recvfrom(other, 512, 0);
      q.sendto(other, b("Welcome"), 0, first[0], Integer.parseInt(first[1]));
    }
    q.close(other);
    Assertions.assertFalse(curr.winning(ref), ref.diffTo(curr, null));
  }

  @Test public void testDatagramSentBeforeBindIsLost()
  {
    PosixEntity p = process(new PosixWorld("early client").addPeer(Peer.udpClient("10.0.0.2", 3000, """
        sendto(3, "Hello?")
        recvfrom(3)
        close(3)
        """)));
    int sock      = p.socket(AF_INET, SOCK_DGRAM, 0); // the client sent before this call, while nothing was bound on 3000
    p.bind(sock, "0.0.0.0", 3000);
    ExplainedException e = Assertions.assertThrows(ExplainedException.class, () -> p.recvfrom(sock, 512, 0));
    Assertions.assertTrue(e.getMessage().contains("recvfrom"), e.getMessage()); // both wait forever
  }

  /** A world where a client on 10.0.0.2 sends Hello and World at once, then Bye, to an echo server on the port 3000 */
  private static PosixWorld echoClient(boolean checking)
  {
    EchoClient client = new EchoClient("10.0.0.2", "Bob", 3000).says(1, "Hello").says(1, "World").says(2, "Bye");
    return new PosixWorld("echo").addPeer(checking ? client.complaining("Broken echo: {0} bytes sent, {1} received") : client);
  }

  /** Receives a datagram on the socket, and sends that echo to its sender */
  private static String echo(PosixEntity p, int sock, String echo)
  {
    String data     = s(p.recvfrom(sock, 512, 0));
    String[] sender = p.lastSender().split(":");
    p.sendto(sock, b(echo == null ? data : echo), 0, sender[0], Integer.parseInt(sender[1]));
    return data;
  }

  @Test public void testEchoClientWaitsForItsEchoes()
  {
    PosixWorld w  = echoClient(true);
    PosixEntity p = process(w);
    int sock      = p.socket(AF_INET, SOCK_DGRAM, 0);
    p.bind(sock, "0.0.0.0", 3000);
    Assertions.assertEquals("Hello", echo(p, sock, null)); // a burst, then the next line once both echoes arrived
    Assertions.assertEquals("World", echo(p, sock, null));
    Assertions.assertEquals("Bye", echo(p, sock, null));
    Assertions.assertTrue(traced(w, "Bob: close(3)"), String.join("\n", w.getTrace()));
    Assertions.assertThrows(ExplainedException.class, () -> p.recvfrom(sock, 512, 0));

    // Without echo, the next line never comes
    PosixEntity q = process(echoClient(true));
    int other     = q.socket(AF_INET, SOCK_DGRAM, 0);
    q.bind(other, "0.0.0.0", 3000);
    Assertions.assertEquals("Hello", s(q.recvfrom(other, 512, 0)));
    Assertions.assertEquals("World", s(q.recvfrom(other, 512, 0)));
    ExplainedException e = Assertions.assertThrows(ExplainedException.class, () -> q.recvfrom(other, 512, 0));
    Assertions.assertTrue(e.getMessage().contains("waiting for the echo of \"Hello\""), e.getMessage());
  }

  @Test public void testEchoClientComplainsAboutAWrongEcho()
  {
    PosixEntity p = process(echoClient(true));
    int sock      = p.socket(AF_INET, SOCK_DGRAM, 0);
    p.bind(sock, "0.0.0.0", 3000);
    echo(p, sock, "Hel"); // truncated
    echo(p, sock, null);
    Assertions.assertEquals("Broken echo: 5 bytes sent, 3 received", s(p.recvfrom(sock, 512, 0)));
    Assertions.assertThrows(ExplainedException.class, () -> p.recvfrom(sock, 512, 0)); // and nothing more

    // A client that does not check accepts any echo
    PosixEntity q = process(echoClient(false));
    int other     = q.socket(AF_INET, SOCK_DGRAM, 0);
    q.bind(other, "0.0.0.0", 3000);
    echo(q, other, "Hel");
    echo(q, other, "W");
    Assertions.assertEquals("Bye", s(q.recvfrom(other, 512, 0)));
  }

  /** The form of Bob's line in UdpEcho, holding the port of the XMPP server */
  private static final String BOB_PORT = "PORT <([0-9]+)>";

  /**
   * Echoes the datagrams arriving on the port 6666 of UdpEcho, received in that buffer, to their sender (or always to the first
   * one), until one matches the pattern (echoed or not), then prints its first group and returns it
   */
  private static String echoUntil(PosixEntity p, int buffer, String pattern, boolean echoTheLast, boolean toTheFirstSender)
  {
    int sock     = p.socket(AF_INET, SOCK_DGRAM, 0);
    String first = null;
    p.bind(sock, "0.0.0.0", 6666);
    while (true) {
      String data = s(p.recvfrom(sock, buffer, 0));
      first       = first == null || !toTheFirstSender ? p.lastSender() : first;
      Matcher m   = Pattern.compile(pattern).matcher(data);
      if (!m.find() || echoTheLast)
        p.sendto(sock, b(data), 0, first.split(":")[0], Integer.parseInt(first.split(":")[1]));
      if (m.find(0)) {
        p.write(1, b(m.group(1) + "\n"));
        p.close(sock);
        return m.group(1);
      }
    }
  }

  @Test public void testUdpEchoStopsOnBobsPort()
  {
    PosixWorld[] worlds = UdpEcho.worlds();
    Assertions.assertEquals("52220", echoUntil(process(worlds[0]), PosixWorld.MAX_DATAGRAM, BOB_PORT, true, false));
    Assertions.assertEquals("15222", echoUntil(process(worlds[1]), PosixWorld.MAX_DATAGRAM, BOB_PORT, true, false));

    // Going on after Bob's line: the late line, then Bob asks whether his line arrived, and then everybody is done
    for (PosixWorld w : UdpEcho.worlds()) {
      ExplainedException e = Assertions.assertThrows(ExplainedException.class, () -> echoUntil(process(w), PosixWorld.MAX_DATAGRAM, "(never)", true, false));
      Assertions.assertTrue(traced(w, "Bob: Did you receive my previous message?"), String.join("\n", w.getTrace()));
      Assertions.assertFalse(e.getMessage().contains("(waiting") || e.getMessage().contains("(sleeping"), e.getMessage());
    }
  }

  @Test public void testUdpEchoGivesTheAccountBeforeThePort()
  {
    String[] passwords = {"ba6915c0de", "70d1e5ca7f"};
    for (int i = 0; i < 2; i++) {
      PosixEntity p   = process(UdpEcho.worlds()[i]);
      int sock        = p.socket(AF_INET, SOCK_DGRAM, 0);
      String accounts = "";
      p.bind(sock, "0.0.0.0", 6666);
      for (String data = ""; !data.matches("(?s).*" + BOB_PORT + ".*");) {
        data = s(p.recvfrom(sock, PosixWorld.MAX_DATAGRAM, 0));
        p.sendto(sock, b(data), 0, p.lastSender().split(":")[0], Integer.parseInt(p.lastSender().split(":")[1]));
        Matcher m = Pattern.compile("(?:USER|PASSWORD) <([0-9a-z]+)>").matcher(data);
        while (m.find())
          accounts += m.group(1) + " ";
      }
      Assertions.assertEquals("newcomer " + passwords[i] + " ", accounts);
      p.close(sock);
    }
  }

  @Test public void testUdpEchoCatchesTheMistakes()
  {
    for (int i = 0; i < 2; i++) {
      PosixWorld expected = UdpEcho.worlds()[i];
      echoUntil(process(expected), PosixWorld.MAX_DATAGRAM, BOB_PORT, true, false);
      PosixWorld w = UdpEcho.worlds()[i];
      echoUntil(process(w), PosixWorld.MAX_DATAGRAM, BOB_PORT, true, false);
      Assertions.assertTrue(w.winning(expected), expected.diffTo(w, null));

      w = UdpEcho.worlds()[i];
      echoUntil(process(w), PosixWorld.MAX_DATAGRAM, "PORT <?([0-9]+)", true, false);
      Assertions.assertFalse(w.winning(expected), "the port without its chevrons");
      w = UdpEcho.worlds()[i];
      echoUntil(process(w), PosixWorld.MAX_DATAGRAM, "PORT <([^>]*)>", true, false);
      Assertions.assertFalse(w.winning(expected), "a port that is not a number");
      w = UdpEcho.worlds()[i];
      echoUntil(process(w), PosixWorld.MAX_DATAGRAM, BOB_PORT, false, false);
      Assertions.assertFalse(w.winning(expected), "Bob's line without its echo");
      // All the echoes to the first sender: Bob waits for the echo of his first line, and never gives the port
      PosixEntity p = process(UdpEcho.worlds()[i]);
      Assertions.assertThrows(ExplainedException.class, () -> echoUntil(p, PosixWorld.MAX_DATAGRAM, BOB_PORT, true, true));

      // A buffer of 1024 bytes truncates a line of Bob, who complains instead of giving the port
      PosixWorld truncating = UdpEcho.worlds()[i];
      Assertions.assertThrows(ExplainedException.class, () -> echoUntil(process(truncating), 1024, BOB_PORT, true, false));
      Assertions.assertTrue(traced(truncating, "Bob: Broken echo"), String.join("\n", truncating.getTrace()));
    }
  }

  @Test public void testUdpScriptErrors()
  {
    IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> Peer.udpServer("10.0.0.1", 53, "sendto(3, \"x\")"));
    Assertions.assertTrue(e.getMessage().contains("recvfrom()"), e.getMessage());
    Assertions.assertThrows(IllegalArgumentException.class, () -> Peer.udpServer("10.0.0.1", 53, "accept(3)"));
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
    Assertions.assertThrows(ExplainedException.class, () -> p.accept(master)); // a deadlock
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

  /** A server of the tests on UDP port 7, answering each datagram with the same in upper case */
  private static final class UdpShoutServer extends ProgramPeer {
    private int fd;

    UdpShoutServer() { super("10.0.0.1", "UDP shout server"); }

    private UdpShoutServer(UdpShoutServer other, Map<FileDescription, FileDescription> copies)
    {
      super(other, copies);
      fd = other.fd;
    }

    @Override protected UdpShoutServer copy(Map<FileDescription, FileDescription> copies) { return new UdpShoutServer(this, copies); }

    @Override protected void start() { fd = bindDatagrams(7); }

    @Override protected boolean act()
    {
      Datagram query = receiveDatagram(fd);
      if (query != null)
        sendDatagram(fd, query.data().toUpperCase(), query.ip(), query.port());
      return query != null;
    }

    @Override protected String waiting() { return "waiting for a datagram"; }
  }

  @Test public void testProgramPeerAnswersDatagrams()
  {
    PosixWorld ref  = new PosixWorld("udp shout").addPeer(new UdpShoutServer());
    PosixWorld curr = (PosixWorld)ref.copy();
    PosixEntity p   = process(ref);
    int sock        = p.socket(AF_INET, SOCK_DGRAM, 0);
    Assertions.assertEquals(5, p.sendto(sock, b("hello"), 0, "10.0.0.1", 7));
    Assertions.assertEquals("HEL", s(p.recvfrom(sock, 3, 0)));
    Assertions.assertEquals("10.0.0.1:7", p.lastSender());
    p.close(sock);

    int other = process(curr).socket(AF_INET, SOCK_DGRAM, 0);
    process(curr).sendto(other, b("hullo"), 0, "10.0.0.1", 7);
    process(curr).close(other);
    Assertions.assertTrue(ref.diffTo(curr, null).contains("received 'hullo'"), ref.diffTo(curr, null)); // the datagrams are compared
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

  /** A world with an IRC server on 10.0.0.1:6667 where Bob answers a polite question, sending in packets of that size if positive */
  private static PosixWorld irc(int packetSize)
  {
    return new PosixWorld("irc").addPeer(
        new IrcServer("10.0.0.1", 6667)
            .channel("#welcome", "Welcome, little buggles!", "Alice", "Bob")
            .say(2, "#welcome", "Alice", "Has anyone seen my brush? I left it near the wall.")
            .answer("Bob", List.of(List.of("please"), List.of("documentation", "docs"), List.of("baggle")), "Thanks! It is /doc.txt", "Sorry, I did not get it")
            .packets(packetSize));
  }

  /** The NOTICE greeting each new client of the IRC server */
  private static final String IRC_GREETING =
      ":irc.buggleinc.com NOTICE * :*** Welcome! Please register with the NICK and USER commands (see the world documentation for details).";

  /** Registers as student, and joins #welcome */
  private static Lines joined(PosixEntity p)
  {
    int sock = p.socket(AF_INET, SOCK_STREAM, 0);
    Assertions.assertEquals(0, p.connect(sock, "10.0.0.1", 6667));
    Lines lines = new Lines(p, sock);
    Assertions.assertEquals(IRC_GREETING, lines.next()); // before the client sends anything
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
      p.send(3, b("privmsg Bob :Please Bob, where is the Documentation? I have a baggle for you.\n"), 0); // any case, and a bare \n
      Assertions.assertEquals(":Alice!alice@buggleinc.com PRIVMSG #welcome :Has anyone seen my brush? I left it near the wall.", lines.next());
      Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG student :Thanks! It is /doc.txt", lines.next());
      p.send(3, b("QUIT\r\n"), 0);
      Assertions.assertEquals("ERROR :Closing Link: 10.0.0.10 (Client Quit)", lines.next());
      Assertions.assertEquals("", s(p.recv(3, 10, 0)));
      p.close(3);
    }
  }

  /** A world where Bob calls the client who did not talk to him, and Alice helps the client who failed to get his answer */
  private static PosixWorld ircWithHints()
  {
    return new PosixWorld("hints").addPeer(new IrcServer("10.0.0.1", 6667)
                                               .channel("#welcome", "Welcome, little buggles!", "Alice", "Bob")
                                               .address(2, "#welcome", "Bob", "{0}: are you looking for something?")
                                               .answer("Bob", List.of(List.of("please"), List.of("baggle", "gift")), "Thanks!", "What?")
                                               .hints("#welcome", "Alice", "Bob", "{0}: be polite", "{0}: say {1} and give a {2}"));
  }

  @Test public void testIrcMembersCallAndHelp()
  {
    PosixEntity p = process(ircWithHints());
    Lines lines   = joined(p);
    Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG #welcome :student: are you looking for something?", lines.next());
    for (String hint : List.of("student: be polite", "student: say please and give a baggle", "student: say please and give a baggle")) {
      p.send(3, b("PRIVMSG Bob :Hello\r\n"), 0);
      Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG student :What?", lines.next());
      Assertions.assertEquals(":Alice!alice@buggleinc.com PRIVMSG #welcome :" + hint, lines.next()); // the last hint is repeated
    }
    p.send(3, b("PRIVMSG Bob :please, a baggle\r\n"), 0);
    Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG student :Thanks!", lines.next());
    Assertions.assertThrows(ExplainedException.class, () -> p.recv(3, 512, 0)); // no more hints: a deadlock
  }

  @Test public void testIrcMembersAnswerOnTheChannel()
  {
    PosixEntity p = process(ircWithHints());
    Lines lines   = joined(p);
    p.send(3, b("PRIVMSG #welcome :Hello everybody\r\nPRIVMSG #welcome :Bob: Hello\r\n"), 0); // only the second one is for Bob
    Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG #welcome :student: What?", lines.next());
    Assertions.assertEquals(":Alice!alice@buggleinc.com PRIVMSG #welcome :student: be polite", lines.next()); // and Bob does not call
    p.send(3, b("PRIVMSG #welcome :bob, please, a gift\r\n"), 0);                                             // any form of each word
    Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG #welcome :student: Thanks!", lines.next());
    Assertions.assertThrows(ExplainedException.class, () -> p.recv(3, 512, 0));
  }

  @Test public void testIrcMembersStayQuietOnceTold()
  {
    PosixEntity p = process(ircWithHints());
    Lines lines   = joined(p);
    p.send(3, b("PRIVMSG Bob :Hello\r\n"), 0); // before Bob calls the client, which he will not do anymore
    Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG student :What?", lines.next());
    p.send(3, b("PRIVMSG Bob :please, a baggle\r\n"), 0); // before the hint of Alice, which she will not give anymore
    Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG student :Thanks!", lines.next());
    Assertions.assertThrows(ExplainedException.class, () -> p.recv(3, 512, 0));
  }

  @Test public void testIrcMemberRefusesPrivateMessages()
  {
    PosixEntity p = process(new PosixWorld("public").addPeer(new IrcServer("10.0.0.1", 6667)
                                                                 .channel("#welcome", "Welcome, little buggles!", "Alice", "Bob")
                                                                 .address(2, "#welcome", "Bob", "{0}: are you looking for something?")
                                                                 .publicOnly("Bob", "Talk to me on the channel")
                                                                 .answer("Bob", List.of(List.of("please")), "Thanks!", "What?")
                                                                 .hints("#welcome", "Alice", "Bob", "{0}: be polite")));
    Lines lines   = joined(p);
    p.send(3, b("PRIVMSG Bob :please\r\n"), 0); // refused even when right, without hint, and Bob still calls the client
    Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG student :Talk to me on the channel", lines.next());
    Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG #welcome :student: are you looking for something?", lines.next());
    p.send(3, b("PRIVMSG #welcome :Bob: please\r\n"), 0);
    Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG #welcome :student: Thanks!", lines.next());
    Assertions.assertThrows(ExplainedException.class, () -> p.recv(3, 512, 0));
  }

  @Test public void testIrcMembersUnderstandAccents()
  {
    PosixWorld w  = new PosixWorld("accents").addPeer(new IrcServer("10.0.0.1", 6667)
                                                          .channel("#welcome", "Welcome, little buggles!", "Alice", "Bob")
                                                          .answer("Bob", List.of(List.of("s'il vous plaît"), List.of("trésor")), "Merci !", "Quoi ?"));
    PosixEntity p = process(w);
    Lines lines   = joined(p);
    p.send(3, "PRIVMSG Bob :Le trésor, s'il vous plait\r\n".getBytes(StandardCharsets.UTF_8), 0); // with or without the accents
    Assertions.assertEquals(":Bob!bob@buggleinc.com PRIVMSG student :Merci !", lines.next());
  }

  @Test public void testIrcServerRefusesTextWithoutColon()
  {
    PosixEntity p = process(irc(0));
    Lines lines   = joined(p);
    p.send(3, b("PRIVMSG #welcome Hello my friends\r\n"), 0);
    Assertions.assertEquals(":irc.buggleinc.com NOTICE student :*** Your message was not sent: a text holding spaces must start with a colon, as in "
                                + "PRIVMSG #welcome :Hello my friends (a real server would only send Hello).",
                            lines.next());
    p.close(3);
  }

  @Test public void testIrcServerErrors()
  {
    PosixEntity p = process(irc(0));
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    p.connect(sock, "10.0.0.1", 6667);
    Lines lines = new Lines(p, sock);
    Assertions.assertEquals(IRC_GREETING, lines.next());
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

  /** A world with the DNS server of buggleinc.lan on 10.0.0.53:53 */
  private static PosixWorld dns()
  {
    return new PosixWorld("dns").addPeer(new DnsServer("10.0.0.53", 53, "buggleinc.lan")
                                             .txt("conference.buggleinc.lan", "Hint 1")
                                             .srv("_xmpp-client._tcp.buggleinc.lan", 10, 5, 6666, "xmpp1.buggleinc.lan")
                                             .srv("_xmpp-client._tcp.buggleinc.lan", 0, 5, 6666, "xmpp2.buggleinc.lan")
                                             .txt("_xmpp-client._tcp.buggleinc.lan", "Hint 2")
                                             .a("xmpp1.buggleinc.lan", "10.0.0.6")
                                             .a("xmpp2.buggleinc.lan", "10.0.200.5"));
  }

  /** The name in the wire format of DNS: each label preceded by its length, then a zero byte */
  private static byte[] dnsName(String name)
  {
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    for (String label : name.split("\\."))
      res.writeBytes(b((char)label.length() + label));
    res.write(0);
    return res.toByteArray();
  }

  /** A query with that id (and the RD flag) asking that type of record of the name, as written by dnsName() */
  private static byte[] dnsQuery(int id, int type, byte[] name)
  {
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    res.writeBytes(new byte[] {(byte)(id >> 8), (byte)id, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0});
    res.writeBytes(name);
    res.writeBytes(new byte[] {(byte)(type >> 8), (byte)type, 0, 1});
    return res.toByteArray();
  }

  /** Sends the query to the DNS server, and returns its answer */
  private static byte[] ask(PosixEntity p, int sock, byte[] query)
  {
    Assertions.assertEquals(query.length, p.sendto(sock, query, 0, "10.0.0.53", 53));
    return p.recvfrom(sock, 512, 0);
  }

  /** The flags (with the response code) of a DNS answer, and its counts of questions, answers, authority and additional records */
  private static List<Integer> dnsHeader(byte[] answer)
  {
    List<Integer> res = new ArrayList<>();
    for (int i = 2; i < 12; i += 2)
      res.add((answer[i] & 0xFF) << 8 | answer[i + 1] & 0xFF);
    return res;
  }

  @Test public void testDnsServerAnswersToTheByte()
  {
    PosixWorld w                   = dns();
    PosixEntity p                  = process(w);
    int sock                       = p.socket(AF_INET, SOCK_DGRAM, 0);
    byte[] query                   = dnsQuery(0x1234, DnsServer.SRV, dnsName("_xmpp-client._tcp.buggleinc.lan"));
    byte[] answer                  = ask(p, sock, query);
    ByteArrayOutputStream expected = new ByteArrayOutputStream();
    expected.writeBytes(new byte[] {0x12, 0x34, (byte)0x85, 0, 0, 1, 0, 2, 0, 0, 0, 1}); // the TXT record comes as additional
    expected.write(query, 12, query.length - 12);
    byte[] owner = dnsName("_xmpp-client._tcp.buggleinc.lan");
    for (String record : List.of("\0\41\0\1\0\0\16\20\0\33\0\12\0\5\32\12" + s(dnsName("xmpp1.buggleinc.lan")),
                                 "\0\41\0\1\0\0\16\20\0\33\0\0\0\5\32\12" + s(dnsName("xmpp2.buggleinc.lan")), "\0\20\0\1\0\0\16\20\0\7\6Hint 2")) {
      expected.writeBytes(owner);
      expected.writeBytes(b(record));
    }
    Assertions.assertEquals(s(expected.toByteArray()), s(answer));
    Assertions.assertTrue(traced(w, "answer NOERROR: SRV 10 5 6666 xmpp1.buggleinc.lan., SRV 0 5 6666 xmpp2.buggleinc.lan.; additional: TXT \"Hint 2\""),
                          w.getTrace().toString());

    answer = ask(p, sock, dnsQuery(7, DnsServer.A, dnsName("xmpp2.buggleinc.lan")));
    Assertions.assertEquals(List.of(0x8500, 1, 1, 0, 0), dnsHeader(answer));
    Assertions.assertEquals("\12\0\310\5", s(answer).substring(answer.length - 4)); // 10.0.200.5
  }

  @Test public void testDnsServerResponseCodes()
  {
    PosixEntity p = process(dns());
    int sock      = p.socket(AF_INET, SOCK_DGRAM, 0);
    // A name with records of other types only: no answer, but its TXT records as additional
    Assertions.assertEquals(List.of(0x8500, 1, 0, 0, 1), dnsHeader(ask(p, sock, dnsQuery(1, DnsServer.A, dnsName("conference.buggleinc.lan")))));
    Assertions.assertEquals(List.of(0x8500, 1, 1, 0, 0), dnsHeader(ask(p, sock, dnsQuery(1, DnsServer.TXT, dnsName("CONFERENCE.buggleinc.lan")))));
    Assertions.assertEquals(List.of(0x8500, 1, 3, 0, 0), dnsHeader(ask(p, sock, dnsQuery(1, DnsServer.ANY, dnsName("_xmpp-client._tcp.buggleinc.lan")))));
    // The names above some records exist, without records (RFC 8020)
    Assertions.assertEquals(List.of(0x8500, 1, 0, 0, 0), dnsHeader(ask(p, sock, dnsQuery(1, DnsServer.A, dnsName("_tcp.buggleinc.lan")))));
    Assertions.assertEquals(List.of(0x8500, 1, 0, 0, 0), dnsHeader(ask(p, sock, dnsQuery(1, DnsServer.A, dnsName("buggleinc.lan")))));
    Assertions.assertEquals(List.of(0x8503, 1, 0, 0, 0), dnsHeader(ask(p, sock, dnsQuery(1, DnsServer.A, dnsName("xmpp3.buggleinc.lan")))));
    Assertions.assertEquals(List.of(0x8105, 1, 0, 0, 0), dnsHeader(ask(p, sock, dnsQuery(1, DnsServer.A, dnsName("www.google.com")))));
    // A single label holding the dots is outside the zone
    Assertions.assertEquals(List.of(0x8105, 1, 0, 0, 0), dnsHeader(ask(p, sock, dnsQuery(1, DnsServer.A, b("\23xmpp1.buggleinc.lan\0")))));
  }

  /** Whether a line of the trace contains the text */
  private static boolean traced(PosixWorld w, String text) { return w.getTrace().stream().anyMatch(line -> line.contains(text)); }

  @Test public void testDnsServerExplainsTheMalformedQueries()
  {
    PosixWorld w  = dns();
    PosixEntity p = process(w);
    int sock      = p.socket(AF_INET, SOCK_DGRAM, 0);
    byte[] query  = dnsQuery(1, DnsServer.A, dnsName("xmpp1.buggleinc.lan"));
    query[5]      = 0; // one question written least significant byte first
    query[4]      = 1;
    byte[] answer = ask(p, sock, query);
    Assertions.assertEquals(List.of(0x8101, 0, 0, 0, 1), dnsHeader(answer)); // the explanation as additional record
    Assertions.assertTrue(traced(w, "network byte order"), w.getTrace().toString());
    String explanation = "FORMERR: The header announces 256 questions (01 00), where a query has exactly one (00 01): "
                         + "the numbers of DNS are written in network byte order, the most significant byte first.";
    // The root name, the type TXT, the class IN, the TTL 3600 and the length of the data, then the length of its single string
    Assertions.assertEquals("\0\0\20\0\1\0\0\16\20\0" + (char)(explanation.length() + 1) + (char)explanation.length() + explanation, s(answer).substring(12));

    byte[] text = dnsQuery(1, DnsServer.A, b("xmpp1.buggleinc.lan\0")); // the name written as text
    Assertions.assertEquals(List.of(0x8101, 0, 0, 0, 1), dnsHeader(ask(p, sock, text)));
    Assertions.assertTrue(traced(w, "The dots of the name are not written"), w.getTrace().toString());

    byte[] padded = new byte[512]; // a whole buffer sent after the query
    query         = dnsQuery(1, DnsServer.A, dnsName("xmpp1.buggleinc.lan"));
    System.arraycopy(query, 0, padded, 0, query.length);
    Assertions.assertTrue(s(ask(p, sock, padded)).contains((512 - query.length) + " bytes follow the question"));
    Assertions.assertTrue(traced(w, (512 - query.length) + " bytes follow the question"), w.getTrace().toString());

    Assertions.assertEquals(5, p.sendto(sock, b("hello"), 0, "10.0.0.53", 53)); // too short: ignored, so the student waits forever
    Assertions.assertThrows(ExplainedException.class, () -> p.recvfrom(sock, 512, 0));
  }

  @Test public void testWorldsWithOtherNamesDiffer()
  {
    // as in the exercises, whose worlds start alike: the combo box selecting a world needs them to differ
    Assertions.assertNotEquals(new PosixWorld("Buggles").addPeer(new IrcServer("10.0.0.1", 194)),
                               new PosixWorld("Turtles").addPeer(new IrcServer("10.0.0.1", 194)));
  }

  @Test public void testComparingOnlySomeAspects()
  {
    PosixWorld ref  = new PosixWorld("aspects").comparing(PosixWorld.Aspect.OUTPUT).addPeer(Peer.server("10.0.0.1", 80, "accept(3) recv(4, 2) close(4)"));
    PosixWorld curr = (PosixWorld)ref.copy();
    for (PosixWorld w : List.of(ref, curr)) {
      PosixEntity p = process(w);
      int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
      p.connect(sock, "10.0.0.1", 80);
      p.send(sock, b(w == ref ? "hi" : "yo"), 0); // the server receives something else, and the socket stays open
      p.write(1, b("done"));
    }
    Assertions.assertFalse(curr.equals(ref));
    Assertions.assertTrue(curr.winning(ref), ref.diffTo(curr, null));
    process(curr).write(1, b("!"));
    Assertions.assertFalse(curr.winning(ref));
  }

  /** A world with an HTTP server on 10.0.0.1:80 serving /doc.txt, sending in packets of that size if positive */
  private static PosixWorld http(int packetSize)
  {
    return new PosixWorld("http").addPeer(new HttpServer("10.0.0.1", 80).file("/doc.txt", "Use RETR.\n").packets(packetSize));
  }

  /** Receives on that descriptor until the data has that length */
  private static String data(PosixEntity p, int fd, int length)
  {
    StringBuilder res = new StringBuilder();
    while (res.length() < length)
      res.append(s(p.recv(fd, length - res.length(), 0)));
    return res.toString();
  }

  private static final String HEADERS = "HTTP/1.1 200 OK\r\nServer: plm-httpd/1.0\r\nDate: Mon, 01 Jan 2024 00:00:00 GMT\r\n"
                                        + "Content-Type: text/plain\r\nContent-Length: 10\r\n";

  @Test public void testHttpServerKeepsTheConnection()
  {
    for (int packetSize : new int[] {0, 16}) {
      PosixWorld w  = http(packetSize);
      PosixEntity p = process(w);
      int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
      Assertions.assertEquals(0, p.connect(sock, "10.0.0.1", 80));
      p.send(sock, b("GET /doc.txt HTTP/1.1\r\nHost: 10.0.0.1\r\n\r\n"), 0);
      String response = HEADERS + "Connection: keep-alive\r\n\r\nUse RETR.\n";
      Assertions.assertEquals(response, data(p, sock, response.length()).replaceFirst("Date: [^\r]*", "Date: Mon, 01 Jan 2024 00:00:00 GMT"));
      p.send(sock, b("HEAD /missing HTTP/1.1\nhost: x\n\n"), 0); // on the same connection, with lines ended by \n alone
      Assertions.assertTrue(data(p, sock, 12).equals("HTTP/1.1 404"));
      p.close(sock);
    }
  }

  @Test public void testHttpServerClosesAfterTheResponse()
  {
    PosixEntity p = process(http(0));
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    p.connect(sock, "10.0.0.1", 80);
    p.send(sock, b("GET /doc.txt HTTP/1.0\r\n\r\n"), 0);
    StringBuilder response = new StringBuilder();
    for (String chunk = s(p.recv(sock, 512, 0)); !chunk.isEmpty(); chunk = s(p.recv(sock, 512, 0)))
      response.append(chunk);
    Assertions.assertEquals(HEADERS + "Connection: close\r\n\r\nUse RETR.\n", response.toString());
    p.close(sock);

    sock = p.socket(AF_INET, SOCK_STREAM, 0);
    p.connect(sock, "10.0.0.1", 80);
    p.send(sock, b("GET /doc.txt HTTP/1.1\r\n\r\n"), 0); // without Host
    Assertions.assertEquals("HTTP/1.1 400 Bad Request\r\n", data(p, sock, 26));
    p.close(sock);
  }

  /** A world with an FTP server on 10.0.0.1:21 serving /pub/secret.txt, sending in packets of that size if positive */
  private static PosixWorld ftp(int packetSize)
  {
    return new PosixWorld("ftp").addPeer(new FtpServer("10.0.0.1", 21).file("/pub/secret.txt", "Baggles!\n").packets(packetSize));
  }

  /** Receives everything until the end of the stream on that descriptor */
  private static String all(PosixEntity p, int fd)
  {
    StringBuilder res = new StringBuilder();
    for (String chunk = s(p.recv(fd, 512, 0)); !chunk.isEmpty(); chunk = s(p.recv(fd, 512, 0)))
      res.append(chunk);
    return res.toString();
  }

  /** Opens a data connection to the port of a 227 reply */
  private static int dataConnection(PosixEntity p, String reply)
  {
    String[] numbers = reply.substring(reply.indexOf('(') + 1, reply.indexOf(')')).split(",");
    int sock         = p.socket(AF_INET, SOCK_STREAM, 0);
    Assertions.assertEquals(0, p.connect(sock, "10.0.0.1", Integer.parseInt(numbers[4]) * 256 + Integer.parseInt(numbers[5])));
    return sock;
  }

  @Test public void testFtpServer()
  {
    for (int packetSize : new int[] {0, 9}) {
      PosixEntity p = process(ftp(packetSize));
      int control   = p.socket(AF_INET, SOCK_STREAM, 0);
      Assertions.assertEquals(0, p.connect(control, "10.0.0.1", 21));
      Lines lines = new Lines(p, control);
      Assertions.assertEquals("220-Welcome to the BuggleInc FTP server.", lines.next());
      Assertions.assertEquals("220 plm-ftpd 1.0 ready.", lines.next());
      p.send(control, b("RETR secret.txt\r\n"), 0);
      Assertions.assertEquals("530 Please login with USER and PASS.", lines.next());
      p.send(control, b("USER anonymous\r\n"), 0);
      Assertions.assertEquals("331 Please specify the password.", lines.next());
      p.send(control, b("PASS buggle@\r\n"), 0);
      Assertions.assertEquals("230 Login successful.", lines.next());
      p.send(control, b("RETR /pub/secret.txt\r\n"), 0);
      Assertions.assertEquals("425 Use PORT or PASV first.", lines.next());
      p.send(control, b("CWD pub\r\nPASV\r\n"), 0);
      Assertions.assertEquals("250 Directory successfully changed.", lines.next());
      String reply = lines.next();
      Assertions.assertEquals("227 Entering Passive Mode (10,0,0,1,156,64).", reply);
      int data = dataConnection(p, reply);
      p.send(control, b("RETR secret.txt\r\n"), 0);
      Assertions.assertEquals("150 Opening ASCII mode data connection for secret.txt (9 bytes).", lines.next());
      Assertions.assertEquals("Baggles!\n", all(p, data));
      Assertions.assertEquals("226 Transfer complete.", lines.next());
      p.close(data);

      p.send(control, b("pasv\n"), 0); // any case, with \n alone
      reply = lines.next();
      p.send(control, b("LIST /\r\n"), 0); // before connecting the data connection
      data = dataConnection(p, reply);
      Assertions.assertEquals("150 Here comes the directory listing.", lines.next());
      Assertions.assertEquals("drwxr-xr-x    1 ftp      ftp          4096 Jan 01  2024 pub\r\n", all(p, data));
      Assertions.assertEquals("226 Directory send OK.", lines.next());
      p.close(data);
      p.send(control, b("RETR missing.txt\r\nQUIT\r\n"), 0);
      Assertions.assertEquals("425 Use PORT or PASV first.", lines.next());
      Assertions.assertEquals("221 Goodbye.", lines.next());
      Assertions.assertEquals("", s(p.recv(control, 10, 0)));
      p.close(control);
    }
  }

  /** The documentation of the world gives the protocol of each kind of program peer once, and nothing for the scripted peers */
  @Test public void testProtocolsDocumentation()
  {
    String about = new PosixWorld("servers")
                       .addPeer(new IrcServer("10.0.0.1", 194))
                       .addPeer(new HttpServer("10.0.0.1", 80))
                       .addPeer(new HttpServer("10.0.0.2", 80))
                       .getAbout();
    Assertions.assertTrue(about.contains("The servers of this world"), about);
    int irc = about.indexOf("The IRC protocol"), http = about.indexOf("The HTTP protocol");
    Assertions.assertTrue(0 <= irc && irc < http, about); // in the order of the peers
    Assertions.assertEquals(1, about.split("The HTTP protocol", -1).length - 1, about);
    Assertions.assertFalse(about.contains("The FTP protocol"), about);

    String scripted = new PosixWorld("scripted").addPeer(Peer.server("10.0.0.1", 80, "accept(3)")).getAbout();
    Assertions.assertFalse(scripted.contains("The servers of this world"), scripted);
    Assertions.assertFalse(scripted.contains("[!protocols/]"), scripted);
  }

  @Test public void testUnlink()
  {
    PosixWorld w  = new PosixWorld("unlink").addFile("notes.txt", "draft");
    PosixEntity p = process(w);
    int fd        = p.open("notes.txt", 0);
    Assertions.assertEquals(0, p.unlink("notes.txt"));
    Assertions.assertEquals(-1, p.open("notes.txt", 0));
    Assertions.assertEquals("ENOENT", p.lastError());
    Assertions.assertEquals(-1, p.unlink("notes.txt"));
    Assertions.assertEquals("", s(p.read(fd, 10))); // the descriptor stays usable
    p.close(fd);
    Assertions.assertTrue(w.winning(new PosixWorld("unlink")), new PosixWorld("unlink").diffTo(w, null));
  }

  @Test public void testScriptEscapes()
  {
    PosixWorld w  = new PosixWorld("escapes").addPeer(Peer.server("10.0.0.1", 80, """
        accept(3)
        send(4, "a\\r\\n\\t\\"\\\\")
        transmit(4, 6)
        close(4)
        """));
    PosixEntity p = process(w);
    int sock      = p.socket(AF_INET, SOCK_STREAM, 0);
    p.connect(sock, "10.0.0.1", 80);
    Assertions.assertEquals("a\r\n\t\"\\", s(p.recv(sock, 10, 0)));
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
