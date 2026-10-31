package lessons.network.bsdsocks;

import plm.core.model.Game;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lesson;
import plm.universe.posix.DnsServer;
import plm.universe.posix.PosixWorld;
import plm.universe.posix.PosixWorld.Aspect;

public class UdpDns extends ExerciseTemplated {
  public UdpDns(Lesson lesson)
  {
    super(lesson, null);
    // The student queries the DNS server as it likes, so only its output and its descriptors are compared. The two XMPP servers
    // swap their priorities between the worlds, so that taking the first or the last SRV record fails one of them, and the address
    // of the Turtles one has a byte above 127, which Java and Scala read as negative.
    setup(new PosixWorld[] {new PosixWorld("Buggles").comparing(Aspect.OUTPUT, Aspect.DESCRIPTORS).addPeer(zone(10, "10.0.0.6", 0, "10.0.0.5")),
                            new PosixWorld("Turtles").comparing(Aspect.OUTPUT, Aspect.DESCRIPTORS).addPeer(zone(0, "10.0.200.5", 10, "10.0.0.7"))});
  }

  /** The zone of buggleinc.lan, whose XMPP servers xmpp1 (listed first) and xmpp2 have these priorities and addresses */
  private static DnsServer zone(int priority1, String ip1, int priority2, String ip2)
  {
    return new DnsServer("10.0.0.53", 53, "buggleinc.lan")
        .txt("buggleinc.lan", Game.i18n.tr(
                                  // Translator: please keep this string unchanged (without the quotes): "conference.buggleinc.lan"
                                  "BuggleInc internal zone. The chat rooms are on conference.buggleinc.lan."))
        .txt("conference.buggleinc.lan", Game.i18n.tr(
                                             // Translator: please keep this string unchanged (without the quotes): "_xmpp-client._tcp.buggleinc.lan"
                                             "The chat rooms are hosted by the XMPP server of buggleinc.lan. As all XMPP clients, look for its SRV record "
                                             + "_xmpp-client._tcp.buggleinc.lan."))
        .srv("_xmpp-client._tcp.buggleinc.lan", priority1, 5, 6666, "xmpp1.buggleinc.lan")
        .srv("_xmpp-client._tcp.buggleinc.lan", priority2, 5, 6666, "xmpp2.buggleinc.lan")
        .txt("_xmpp-client._tcp.buggleinc.lan",
             Game.i18n.tr(
                 // Translator: please keep this string unchanged (without the quotes): "6666"
                 "6666 is a decoy. To get the real port, start a UDP echo server on the port 6666 of your own machine, and the hint will "
                 + "come to you."))
        .a("xmpp1.buggleinc.lan", ip1)
        .a("xmpp2.buggleinc.lan", ip2);
  }
}
