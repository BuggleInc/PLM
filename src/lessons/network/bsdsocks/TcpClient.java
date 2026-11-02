package lessons.network.bsdsocks;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.posix.Peer;
import plm.universe.posix.PosixWorld;

public class TcpClient extends ExerciseTemplated {
  public TcpClient(Lesson lesson)
  {
    super(lesson, null);
    // The parameters of client(): the address and the port of the server. The buggles and the turtles chat in the first worlds,
    // while Bob starts the protocols quest in the last one, in a message that the network splits in packets.
    setup(new PosixWorld[] {
        world("Greetings", "10.0.0.1", 9001, false, Game.i18n.tr("Hello, world! Alice the buggle here: I painted the whole floor blue today.")),
        world("Weather", "10.0.0.2", 8080, false, Game.i18n.tr("Sunny over the pond of the turtles: 25 degrees, a perfect day to draw spirals.")),
        // printf(buffer) interprets the %s of the data, which is a classical security hole
        world("Malicious", "10.0.0.1", 9001, false,
              Game.i18n.tr(
                  // Translator: please keep this string unchanged (without the quotes): "%s"
                  "Dave was here, and he painted %s on the wall. Never trust what comes from the network!")),
        world("Booting", "10.0.0.1", 80, true, Game.i18n.tr("Sorry for the delay: my turtle was finishing its spiral.")),
        packets("Packets", "10.0.0.3", 9001,
                Game.i18n.tr("Psst, newcomer! Bob here. I know where a real treasure is hidden, but the walls have ears. "
                             + "Come and see me on the IRC server of BuggleInc, and I will tell you more."))});
  }

  /**
   * A world where the server sends the text one second after accepting the connection, and the network transmits it one second
   * later. A booting server refuses the connections during its first second.
   */
  private static PosixWorld world(String name, String ip, int port, boolean booting, String text)
  {
    return new PosixWorld(name).setParameters(ip, port).addPeer(
        Peer.server(ip, port, """
        %saccept(3)
        sleep(1)
        send(4, %s)
        sleep(1)
        transmit(4, %d)
        close(4)
        """.formatted(booting ? "sleep(1) // connections are refused meanwhile\n" : "", Peer.literal(text), Peer.ascii(text).length())));
  }

  /** A world where the server sends the text in three parts, which the network splits in packets of various sizes and paces */
  private static PosixWorld packets(String name, String ip, int port, String text)
  {
    String ascii = Peer.ascii(text);
    int first = ascii.indexOf(' ', ascii.length() / 3) + 1, second = ascii.indexOf(' ', 2 * ascii.length() / 3) + 1;
    return new PosixWorld(name).setParameters(ip, port).addPeer(Peer.server(
        ip, port, """
        accept(3)
        sleep(1)
        sendInPackets(4, %s, 7, 1)
        sleep(1)
        sendInPackets(4, %s, 5, 1)
        sleep(1)
        sendInPackets(4, %s, 13, 1)
        close(4)
        """.formatted(Peer.literal(ascii.substring(0, first)), Peer.literal(ascii.substring(first, second)), Peer.literal(ascii.substring(second)))));
  }
}
