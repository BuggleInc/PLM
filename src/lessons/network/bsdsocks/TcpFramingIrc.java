package lessons.network.bsdsocks;

import java.util.List;
import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.posix.IrcServer;
import plm.universe.posix.PosixWorld;
import plm.universe.posix.PosixWorld.Aspect;

public class TcpFramingIrc extends ExerciseTemplated {
  public TcpFramingIrc(Lesson lesson)
  {
    super(lesson, null);
    // The student talks with the IRC server as it likes (nickname, wording of the messages), so only its output and its
    // descriptors are compared. Bob answers the messages containing the polite words, the same in both worlds so that the same
    // code wins both, in private or on the channel when they start with "Bob:". He calls the student who did not talk to him yet,
    // and Dave helps the student who failed to get his answer, with hints whose {0} is the nickname of the student and {1}, {2}
    // the words that Bob expects (the patterns of MessageFormat double their '). Bob accepts the English words in any language.
    List<List<String>> polite = List.of(List.of(Game.i18n.tr("please"), "please"), List.of(Game.i18n.tr("treasure"), "treasure"));
    IrcServer buggles =
        new IrcServer("10.0.0.1", 194)
            .channel("#welcome", Game.i18n.tr("Welcome, little buggles!"), "Alice", "Bob", "Carol", "Dave")
            .say(1, "#welcome", "Alice", Game.i18n.tr("Has anyone seen my brush? I left it near the wall."))
            .address(2, "#welcome", "Bob", Game.i18n.tr("{0}: are you looking for something? Talk to me with \"PRIVMSG #welcome :Bob: your message\"."))
            .say(3, "#welcome", "Carol", Game.i18n.tr("I painted the whole floor blue today, it looks like the sea!"))
            .say(5, "#welcome", "Dave", Game.i18n.tr("Turning left three times is the same as turning right, I swear."))
            .say(7, "#welcome", "Alice", Game.i18n.tr("Found it! It was under a baggle."))
            .say(9, "#welcome", "Carol", Game.i18n.tr("I let my turmite run all night: after ten thousand steps, it built a highway."))
            .say(11, "#welcome", "Dave", Game.i18n.tr("In a maze, I keep a paw on the wall at my right. It always works."))
            .say(13, "#welcome", "Alice", Game.i18n.tr("Not when the maze has islands, Dave! Then you must count your turns."))
            .say(15, "#welcome", "Carol", Game.i18n.tr("My turmite drew a square spiral, then got lost in a maze of its own."))
            .answer("Bob", polite,
                    Game.i18n.tr(
                        // Translator: please keep this string unchanged (without the quotes): "<xmpp:robots@conference.buggleinc.lan>"
                        "Hold on, pal. We should not speak on an open IRC channel. Go see my old friend Karel on this channel: "
                        + "<xmpp:robots@conference.buggleinc.lan> Use the BuggleInc DNS server to find it, and pay attention to the details while doing so."),
                    Game.i18n.tr("Hmm? Buggles who want to know things ask nicely, with the right words."))
            .hints("#welcome", "Dave", "Bob", Game.i18n.tr("{0}: you should ask Bob again, but this time be polite."),
                   Game.i18n.tr("{0}: Bob likes the magic word, and to know what you look for: the treasure trove."),
                   Game.i18n.tr("{0}: write exactly this here: Bob: {1}, tell me the way to the {2}."));
    IrcServer turtles =
        new IrcServer("10.0.0.1", 194)
            .channel("#welcome", Game.i18n.tr("Slow and steady wins the race."), "Alice", "Bob", "Carol", "Dave")
            .say(1, "#welcome", "Alice", Game.i18n.tr("I drew a perfect circle today: 360 steps of one degree!"))
            .address(2, "#welcome", "Bob", Game.i18n.tr("{0}: are you looking for something? I know the path to a real treasure!"))
            .say(3, "#welcome", "Carol", Game.i18n.tr("Pen up or pen down? I always forget which one I am in."))
            .say(5, "#welcome", "Dave", Game.i18n.tr("I tried a spiral, and got dizzy after the third turn."))
            .say(7, "#welcome", "Alice", Game.i18n.tr("Careful, Dave, last time you drew yourself into a corner."))
            .say(9, "#welcome", "Carol", Game.i18n.tr("To draw a tree, draw a trunk, then two smaller trees on top. That is all!"))
            .say(11, "#welcome", "Dave", Game.i18n.tr("I tried, but I forgot to stop when the branches get too small. I am still drawing."))
            .say(13, "#welcome", "Alice", Game.i18n.tr("My Koch snowflake has four levels now. The fifth one will take all afternoon."))
            .say(15, "#welcome", "Carol", Game.i18n.tr("Recursion is just like us: it is turtles all the way down."))
            .answer("Bob", polite,
                    Game.i18n.tr(
                        // Translator: please keep this string unchanged (without the quotes): "<xmpp:lsystem@conference.buggleinc.lan>"
                        "Not so quick, my friend. We should not speak on an open IRC channel. Go see my old friend Karol on this channel: "
                        + "<xmpp:lsystem@conference.buggleinc.lan> Use the BuggleInc DNS server to find it, and pay attention to the details while doing so."),
                    Game.i18n.tr("Turtles take their time, but they like polite requests."))
            .hints("#welcome", "Dave", "Bob", Game.i18n.tr("{0}: ask Bob again, and be polite this time."),
                   Game.i18n.tr("{0}: Bob wants the magic word, and to know what you look for: the treasure."),
                   Game.i18n.tr("{0}: say exactly this here: Bob: {1}, tell me the way to the {2}."))
            .packets(11); // the lines of the members arrive in pieces
    setup(new PosixWorld[] {new PosixWorld("Buggles").comparing(Aspect.OUTPUT, Aspect.DESCRIPTORS).redactingTheObjective().addPeer(buggles),
                            new PosixWorld("Turtles").comparing(Aspect.OUTPUT, Aspect.DESCRIPTORS).redactingTheObjective().addPeer(turtles)});
  }
}
