package lessons.network.bsdsocks;

import java.util.List;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.posix.PosixWorld;
import plm.universe.posix.PosixWorld.Aspect;
import plm.universe.posix.XmppServer;

public class XmppClient extends ExerciseTemplated {
  public XmppClient(Lesson lesson)
  {
    super(lesson, null);
    setup(worlds());
  }

  /**
   * The worlds of the exercise, where the parameters of client() are the address and the port of the XMPP server (given by UdpDns
   * and UdpEcho), the address of the room (given by TcpFramingIrc), the password of the newcomer (given by UdpEcho) and the
   * nickname of the friend of Bob (Karel or Karol). The friend is away until he calls the student, two seconds after it joined
   * the room: he answers an automatic reply to the messages written before. He then answers the messages addressed to him that
   * hold the words he expects (who sent the student and what it looks for, the same in all the worlds), while an occupant helps
   * the student who failed with hints whose {0} is the nickname of the student and {2} the word for the treasure ({1} being bob,
   * in lower case). The student talks as it likes, so only its output and its descriptors are compared. The worlds get harder:
   * the stanzas arrive whole in the first one, cut in packets with spaces between them in the second one, and the third one adds
   * stanzas nesting elements of the same name, such as an old message of Karel forwarded inside a message of Carol.
   */
  public static PosixWorld[] worlds()
  {
    XmppServer turtles =
        new XmppServer("10.0.200.5", UdpEcho.TURTLES_PORT)
            .account("newcomer", UdpEcho.TURTLES_PASSWORD)
            .room("lsystem", Game.i18n.tr("Turtles, cats and sprites: everybody draws here."), "Alice", "Carol", "Dave", "Gobo", "Karol", "Pico", "Scratch")
            .history("lsystem", "Scratch", Game.i18n.tr("I drew a cat with twelve blocks. A cat drawing a cat!"))
            .history("lsystem", "Gobo", Game.i18n.tr("Karol, can you draw with your bricks?"))
            .history("lsystem", "Karol", Game.i18n.tr("I lay bricks, I do not draw them. It is not the same."))
            .say(1, "lsystem", "Alice", Game.i18n.tr("Pen down, everybody: our new friend is coming."))
            .address(2, "lsystem", "Karol",
                     Game.i18n.tr("Back, sorry. Oh, hello {0}, you are here! Welcome to the lsystem room. Who sent you, and what do you seek?"))
            .away("Karol", Game.i18n.tr("Automatic reply: Karol is away from his keyboard. He will call you when he is back."))
            .say(3, "lsystem", "Scratch", Game.i18n.tr("Meow. When the green flag is clicked, I say hello & spin."))
            .say(5, "lsystem", "Carol", Game.i18n.tr("My plant grows with F -> F[+F]F[-F]F. Five steps, and it fills the screen."))
            .say(7, "lsystem", "Pico", Game.i18n.tr("Is 3 < 4 in turtle geometry too?"))
            .say(9, "lsystem", "Dave", Game.i18n.tr("I turned 360 degrees and ended where I started. Is that progress?"))
            .answer("Karol", words(),
                    Game.i18n.tr(
                        // Translator: please keep this string unchanged (without the quotes): "[http://www.buggleinc.lan/hunt/turtles.html]"
                        "Ah, the friend of Bob. The hunt goes on on the web, from this page: [http://www.buggleinc.lan/hunt/turtles.html] "
                        + "Take your time, and read each page whole."),
                    Game.i18n.tr("Slowly, slowly. Tell me who sent you, and what you seek."))
            .hints("lsystem", "Scratch", "Karol", Game.i18n.tr("{0}: Karol only helps the friends of Bob. Tell him who sent you. Meow."),
                   Game.i18n.tr("{0}: tell Karol that Bob sent you, and that you seek the {2}."),
                   Game.i18n.tr("{0}: write exactly this here: Karol: Bob sent me for the {2}."))
            .addressingHint("lsystem", "Scratch", "Karol",
                            Game.i18n.tr("{0}: Karol only reads the messages that start with his name and a colon, like this: Karol: hello. Meow."))
            .packets(61) // the stanzas arrive in pieces, cut anywhere
            .keepAlives()
            .doubleQuotes();
    XmppServer busy = buggles()
                          .sayInXhtml(4, "robots", "Karel++", Game.i18n.tr("I speak in XHTML now: my messages have two bodies!"))
                          .forward(0, "robots", "Carol", Game.i18n.tr("Wait! Karel already told us where the hunt starts, last year:"), "Karel",
                                   Game.i18n.tr(
                                       // Translator: please keep this string unchanged (without the quotes): "[http://www.buggleinc.lan/hunt/2023.html]"
                                       "Welcome, friend of Bob. The hunt starts on this page: [http://www.buggleinc.lan/hunt/2023.html]"),
                                   "2023-12-24T18:00:00Z")
                          .afterWritingTo("Karel") // just before his answer
                          .packets(47)
                          .keepAlives();
    return new PosixWorld[] {world("Buggles", "10.0.0.5", UdpEcho.BUGGLES_PORT, "robots", UdpEcho.BUGGLES_PASSWORD, "Karel", buggles()),
                             world("Turtles", "10.0.200.5", UdpEcho.TURTLES_PORT, "lsystem", UdpEcho.TURTLES_PASSWORD, "Karol", turtles),
                             world("Busy buggles", "10.0.0.5", UdpEcho.BUGGLES_PORT, "robots", UdpEcho.BUGGLES_PASSWORD, "Karel", busy)};
  }

  /** The words that the friend of Bob expects, in any language: who sent the student, and what it looks for */
  private static List<List<String>> words() { return List.of(List.of("Bob"), List.of(Game.i18n.tr("treasure"), "treasure")); }

  /** The XMPP server of the buggles, whose room robots has Karel, sending each stanza whole (quoting with " as in every world) */
  private static XmppServer buggles()
  {
    return new XmppServer("10.0.0.5", UdpEcho.BUGGLES_PORT)
        .account("newcomer", UdpEcho.BUGGLES_PASSWORD)
        .doubleQuotes()
        .room("robots", Game.i18n.tr("Robots and buggles. No beepers on the floor, please."), "Alice", "Carol", "Dave", "Guido", "Karel", "Karel++")
        .history("robots", "Karel++", Game.i18n.tr("I can turn right now! I have a method for that."))
        .history("robots", "Guido", Game.i18n.tr("Show-off. I turn right by turning left three times, as any honest robot."))
        .history("robots", "Karel", Game.i18n.tr("Beep. I picked up 42 beepers today."))
        .say(1, "robots", "Alice", Game.i18n.tr("Hello robots! Has anyone seen our new friend?"))
        .address(2, "robots", "Karel",
                 Game.i18n.tr("Back, sorry. Oh, hello {0}, you are here! So you are the one Bob talked about? Tell me what brings you here."))
        .away("Karel", Game.i18n.tr("Automatic reply: Karel is away from his keyboard. He will call you when he is back."))
        .say(3, "robots", "Carol", Game.i18n.tr("Karel, your beepers look just like our baggles."))
        .say(5, "robots", "Karel++", Game.i18n.tr("A baggle is a beeper with methods & attributes."))
        .say(7, "robots", "Dave", Game.i18n.tr("I tried to eat a beeper once. It does not taste like a biscuit."))
        .say(9, "robots", "Guido", Game.i18n.tr("In my world, a wall stands where x < 2. Do not ask why."))
        .answer("Karel", words(),
                Game.i18n.tr(
                    // Translator: please keep this string unchanged (without the quotes): "[http://www.buggleinc.lan/hunt/buggles.html]"
                    "Welcome, friend of Bob. The hunt goes on on the web, from this page: [http://www.buggleinc.lan/hunt/buggles.html] "
                    + "Read it carefully: the server likes to send you elsewhere."),
                Game.i18n.tr("Beep? I only talk to the friends of Bob, and only about one thing."))
        .hints("robots", "Alice", "Karel", Game.i18n.tr("{0}: Karel only trusts the friends of Bob. Tell him who sent you."),
               Game.i18n.tr("{0}: tell Karel that Bob sent you, and what you look for: the {2}."),
               Game.i18n.tr("{0}: write exactly this here: Karel: Bob sent me for the {2}."))
        .addressingHint("robots", "Alice", "Karel",
                        Game.i18n.tr("{0}: Karel only reads the messages that start with his name and a colon, like this: Karel: hello."));
  }

  /** A world where client() gets these parameters, and the student finds this server */
  private static PosixWorld world(String name, String ip, int port, String room, String password, String friend, XmppServer server)
  {
    return new PosixWorld(name)
        .holdingTranslations()
        .setParameters(ip, port, room + "@" + XmppServer.CONFERENCE, password, friend)
        .comparing(Aspect.OUTPUT, Aspect.DESCRIPTORS)
        .redactingTheObjective()
        .addPeer(server);
  }
}
