package lessons.network.bsdsocks;

import java.util.ArrayList;
import java.util.List;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.posix.Peer;
import plm.universe.posix.PosixWorld;

public class UdpEcho extends ExerciseTemplated {
  /** The port of the echo server, given by the hint of the DNS server (see UdpDns) */
  private static final int PORT = 6666;

  /** A line of the chat, sent by that character to the echo server at that time (from 1, once the student bound its socket) */
  private record Line(int time, String who, String text) {}

  public UdpEcho(Lesson lesson)
  {
    super(lesson, null);
    setup(worlds());
  }

  /**
   * The worlds of the exercise, where the parameter of server() is the port to bind. Each character sends its lines from its own
   * machine and waits for their echo, so that every aspect is compared: each line must come back whole to its sender. Bob's line
   * holding PORT <n> gives the port of the XMPP server, which differs between the worlds, while other lines look like it without
   * its exact form. A line sent at the same time as Bob's, after it, stays unanswered, as the student stops on Bob's line. Bob
   * then asks whether his line arrived, which only a student going on after it receives (before an explained deadlock).
   */
  public static PosixWorld[] worlds()
  {
    PosixWorld buggles = new Chat("Alice", "Carol", "Bob", "Dave")
                             .says(1, "Alice", Game.i18n.tr("Testing, testing. Is the echo server of the newcomer up?"))
                             .says(1, "Carol", Game.i18n.tr("Hello? Hello? Oh, it answers with my own words!"))
                             .says(2, "Dave", Game.i18n.tr("Echo! Echo! Echo!"))
                             .says(3, "Alice",
                                   Game.i18n.tr(
                                       // Translator: please keep this string unchanged (without the quotes): "PORT <number>"
                                       "Bob says that he will give the PORT <number> to the newcomer once the echo works. Patience."))
                             .says(4, "Carol", Game.i18n.tr("Does it echo the baggles too? Here is one: (o)"))
                             .says(5, "Dave",
                                   Game.i18n.tr(
                                       // Translator: please keep this string unchanged (without the quotes): "PORT 5234"
                                       "I heard that the XMPP server is not on PORT 5234 anymore."))
                             .says(5, "Dave", Game.i18n.tr("Or was it 5243? I always swap my digits."))
                             .says(6, "Bob", Game.i18n.tr("Hold on, everybody. I am checking that the echo is faithful."))
                             .says(7, "Alice", Game.i18n.tr("I paint the echoes in red, to tell them from my own words."))
                             .says(8, "Carol", Game.i18n.tr("A turmite on a wall, an echo in a socket: they always come back."))
                             .says(9, "Dave", Game.i18n.tr("In a maze, I shout and listen to the echo to find the walls."))
                             .says(10, "Alice", Game.i18n.tr("That is not how mazes work, Dave."))
                             .says(10, "Carol", Game.i18n.tr("Unless the maze is made of sockets."))
                             .says(11, "Bob", Game.i18n.tr("The echo is good. Newcomer, listen carefully now."))
                             .says(12, "Bob",
                                   Game.i18n.tr(
                                       // Translator: please keep this string unchanged (without the quotes): "PORT <{0}>"
                                       "Karel waits for you on PORT <{0}> of the XMPP server whose address the DNS gave you. Hurry up!", String.valueOf(52220)))
                             .says(12, "Dave", Game.i18n.tr("Wait, what did Bob say? Can you repeat?"))
                             .says(14, "Bob", Game.i18n.tr("Did you receive my previous message?"))
                             .world("Buggles");
    PosixWorld turtles = new Chat("Carol", "Dave", "Bob", "Alice")
                             .says(1, "Alice", Game.i18n.tr("Is this the echo server of the newcomer? Hello, hello, hello..."))
                             .says(2, "Dave", Game.i18n.tr("Slow and steady: one word per second. Echo."))
                             .says(3, "Carol", Game.i18n.tr("Here is my Koch curve at level 4. Does it come back whole?") + " " + koch(4))
                             .says(4, "Dave", Game.i18n.tr("Echo."))
                             .says(4, "Alice", Game.i18n.tr("Carol, your curve took the whole queue!"))
                             .says(5, "Bob",
                                   Game.i18n.tr(
                                       // Translator: please keep this string unchanged (without the quotes): "PORT <number>"
                                       "Patience, everybody. The newcomer gets the PORT <number> once the echo works."))
                             .says(6, "Carol",
                                   Game.i18n.tr(
                                       // Translator: please keep this string unchanged (without the quotes): "PORT 5234"
                                       "The old map says PORT 5234, but the map is older than me."))
                             .says(7, "Dave", Game.i18n.tr("Echo, one."))
                             .says(7, "Dave", Game.i18n.tr("Echo, two."))
                             .says(8, "Alice", Game.i18n.tr("An echo is a spiral of sound, smaller at each turn."))
                             .says(9, "Carol", Game.i18n.tr("Then it is recursive, and it needs a stop condition!"))
                             .says(9, "Dave", Game.i18n.tr("My stop condition is a nap."))
                             .says(10, "Bob", Game.i18n.tr("The echo is faithful. Newcomer, here is what you are waiting for."))
                             .says(11, "Bob",
                                   Game.i18n.tr(
                                       // Translator: please keep this string unchanged (without the quotes): "PORT <{0}>"
                                       "Karol waits for you on PORT <{0}> of the XMPP server whose address the DNS gave you. Take your time, but not too much.",
                                       String.valueOf(15222)))
                             .says(11, "Alice", Game.i18n.tr("Bob, can you say it again? I was drawing a circle."))
                             .says(13, "Bob", Game.i18n.tr("Did you receive my previous message?"))
                             .world("Turtles");
    return new PosixWorld[] {buggles, turtles};
  }

  /** A chat between characters, each on its own machine, sending their lines to the echo server and waiting for their echo */
  private static final class Chat {
    /** The characters, in the order of their machines, which is also the order of their lines sent at the same time */
    private final List<String> characters;
    /** The lines, in the order of their time */
    private final List<Line> lines = new ArrayList<>();

    Chat(String... characters) { this.characters = List.of(characters); }

    /** Adds a line, sent at that time (after the previous ones) by that character */
    Chat says(int time, String who, String text)
    {
      lines.add(new Line(time, who, text));
      return this;
    }

    /** The world of this chat, where the parameter of server() is the port to bind */
    PosixWorld world(String name)
    {
      PosixWorld world = new PosixWorld(name).setParameters(PORT).redactingTheObjective();
      for (int i = 0; i < characters.size(); i++)
        world.addPeer(Peer.udpClient("10.0.0." + (21 + i), PORT, script(characters.get(i))));
      return world;
    }

    /**
     * The script of a character sending its lines at their time, prefixed by its name: the lines of a same time are sent in a
     * burst, and their echoes are awaited before going on.
     */
    private String script(String who)
    {
      StringBuilder script = new StringBuilder();
      int clock            = 0;
      int burst            = 0;
      for (Line line : lines) {
        if (!line.who().equals(who))
          continue;
        if (line.time() > clock) { // a new burst: wait for the echoes of the previous one, then for the time of this one
          script.append("recvfrom(3)\n".repeat(burst)).append("sleep(").append(line.time() - clock).append(")\n");
          clock = line.time();
          burst = 0;
        }
        String data = Peer.ascii(who + ": " + line.text());
        script.append("sendto(3, \"").append(data.replace("\\", "\\\\").replace("\"", "\\\"")).append("\")\n");
        burst++;
      }
      return script.append("recvfrom(3)\n".repeat(burst)).append("close(3)\n").toString();
    }
  }

  /** The quadratic Koch curve at that level, as an L-system: F draws a segment, + and - turn by 90 degrees */
  private static String koch(int level)
  {
    String curve = "F";
    for (int i = 0; i < level; i++)
      curve = curve.replace("F", "F+F-F-F+F");
    return curve;
  }
}
