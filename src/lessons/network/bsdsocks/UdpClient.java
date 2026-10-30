package lessons.network.bsdsocks;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.posix.Peer;
import plm.universe.posix.PosixWorld;

public class UdpClient extends ExerciseTemplated {
  /** An answer longer than the usual buffers of 512 bytes, which a smaller buffer of recvfrom() truncates */
  private static final String LONG_ANSWER = "A datagram arrives whole, or not at all. ".repeat(20);

  public UdpClient(Lesson lesson)
  {
    super(lesson, null);
    // The parameters of client(): the address and the port of the server, and the request to send it. Each server answers the
    // first datagram it receives with one datagram, to its sender.
    setup(new PosixWorld[] {
        new PosixWorld("Greetings").setParameters("10.0.0.1", 9001, "Hello?").addPeer(Peer.udpServer("10.0.0.1", 9001, """
                                recvfrom(3)
                                sendto(3, "Hello, world!")
                                close(3)
                                """)),
        new PosixWorld("Weather").setParameters("10.0.0.2", 8080, "Weather in Rennes?").addPeer(Peer.udpServer("10.0.0.2", 8080, """
                                recvfrom(3)
                                sendto(3, "Sunny, 25 degrees in Rennes")
                                close(3)
                                """)),
        // printf(buffer) interprets the %s of the data, which is a classical security hole
        new PosixWorld("Malicious").setParameters("10.0.0.1", 9001, "Hello?").addPeer(Peer.udpServer("10.0.0.1", 9001, """
                                recvfrom(3)
                                sendto(3, "Never trust %s from the network")
                                close(3)
                                """)),
        // A buffer smaller than the datagram loses its end
        new PosixWorld("Long answer").setParameters("10.0.0.3", 9001, "Tell me more").addPeer(Peer.udpServer("10.0.0.3", 9001, """
                                recvfrom(3)
                                sendto(3, "%s")
                                close(3)
                                """.formatted(LONG_ANSWER)))});
  }
}
