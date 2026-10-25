package lessons.network.bsdsocks;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.posix.Peer;
import plm.universe.posix.PosixWorld;

public class TcpServer extends ExerciseTemplated {
  public TcpServer(Lesson lesson)
  {
    super(lesson, null);
    // The parameter of server(): its port. Each client sends a line ended by \n, and waits for it twice before closing.
    setup(new PosixWorld[] {new PosixWorld("Ping").setParameters(3000).addPeer(Peer.client("10.0.0.2", 3000, """
                                sleep(1)
                                connect(3)
                                send(3, "PING\\n")
                                sleep(1)
                                transmit(3, 5)
                                recv(3, 10)  // the line, twice
                                close(3)
                                """)),
                            new PosixWorld("Two packets").setParameters(3000).addPeer(Peer.client("10.0.0.3", 3000, """
                                sleep(1)
                                connect(3)
                                sendInPackets(3, "Is anybody there?\\n", 9, 1)
                                recv(3, 36)  // the line, twice
                                close(3)
                                """)),
                            new PosixWorld("Slow client").setParameters(4000).addPeer(Peer.client("10.0.0.4", 4000, """
                                sleep(1)
                                connect(3)
                                sendInPackets(3, "Hi\\n", 1, 1)
                                recv(3, 6)  // the line, twice
                                close(3)
                                """))});
  }
  }
