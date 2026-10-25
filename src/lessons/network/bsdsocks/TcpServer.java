package lessons.network.bsdsocks;

import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.posix.Peer;
import plm.universe.posix.PosixWorld;

public class TcpServer extends ExerciseTemplated {
  public TcpServer(Lesson lesson)
  {
    super(lesson, null);
    // The parameters of server(): its port, and the message to send. Each client receives the whole message, then closes.
    setup(new PosixWorld[] {new PosixWorld("Greetings").setParameters(3000, "Hello, client!").addPeer(Peer.client("10.0.0.2", 3000, """
                                sleep(1)
                                connect(3)
                                recv(3, 14)
                                close(3)
                                """)),
                            new PosixWorld("Other port").setParameters(4000, "Welcome on the port 4000.").addPeer(Peer.client("10.0.0.3", 4000, """
                                sleep(1)
                                connect(3)
                                recv(3, 25)
                                close(3)
                                """)),
                            // accept() waits until the client connects
                            new PosixWorld("Late client").setParameters(3000, "You took your time!").addPeer(Peer.client("10.0.0.4", 3000, """
                                sleep(3)
                                connect(3)
                                recv(3, 19)
                                close(3)
                                """))});
  }
  }
