package lessons.network.bsdsocks;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.posix.Peer;
import plm.universe.posix.PosixWorld;

public class TcpClient extends ExerciseTemplated {
  public TcpClient(Lesson lesson)
  {
    super(lesson, null);
    // The parameters of client(): the address and the port of the server
    setup(new PosixWorld[] {new PosixWorld("Greetings").setParameters("10.0.0.1", 9001).addPeer(Peer.server("10.0.0.1", 9001, """
                                accept(3)
                                sleep(1)
                                send(4, "Hello, world!")
                                sleep(1)
                                transmit(4, 13)
                                close(4)
                                """)),
                            new PosixWorld("Weather").setParameters("10.0.0.2", 8080).addPeer(Peer.server("10.0.0.2", 8080, """
                                accept(3)
                                sleep(1)
                                send(4, "Sunny, 25 degrees in Rennes")
                                sleep(1)
                                transmit(4, 27)
                                close(4)
                                """)),
                            // printf(buffer) interprets the %s of the data, which is a classical security hole
                            new PosixWorld("Malicious").setParameters("10.0.0.1", 9001).addPeer(Peer.server("10.0.0.1", 9001, """
                                accept(3)
                                sleep(1)
                                send(4, "Never trust %s from the network")
                                sleep(1)
                                transmit(4, 31)
                                close(4)
                                """)),
                            new PosixWorld("Booting").setParameters("10.0.0.1", 80).addPeer(Peer.server("10.0.0.1", 80, """
                                sleep(1) // connections are refused meanwhile
                                accept(3)
                                sleep(1)
                                send(4, "Sorry for the delay.")
                                sleep(1)
                                transmit(4, 20)
                                close(4)
                                """)),
                            // The words are split between the packets, which arrive at various paces
                            new PosixWorld("Packets").setParameters("10.0.0.3", 9001).addPeer(Peer.server("10.0.0.3", 9001, """
                                accept(3)
                                sleep(1)
                                sendInPackets(4, "Hello, world! ", 7, 1)
                                sleep(1)
                                sendInPackets(4, "The network splits long messages ", 5, 1)
                                sleep(1)
                                sendInPackets(4, "in packets.", 13, 1)
                                close(4)
                                """))});
  }
  }
