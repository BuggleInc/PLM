package plm.universe.posix;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import plm.core.model.Game;

/**
 * An XMPP server (RFC 6120 and 6121) for the domain buggleinc.lan, with its multi-user chat service (XEP-0045) on
 * conference.buggleinc.lan, exact to the byte as the usual servers on what it covers. Each connection carries a stream per
 * direction: an XML document whose top-level elements are the stanzas, framed by the nesting of their elements only (see Xml). The
 * client opens its stream, authenticates with SASL PLAIN (without TLS, which the server does not offer), may restart its stream
 * as RFC 6120 asks, then talks without binding a resource: the server takes its bare JID as address. It joins the chat rooms with
 * a presence, gets the presences of the occupants, the history of the room (with its delays) and its subject, then the messages
 * of the room. Its messages to a room come back to it, as to every occupant. The other occupants are simulated, as the members of
 * the channels of IrcServer: they speak at given times after the client joined, answer the messages addressed to them (as in
 * "Karel: hello") or sent to them in private, depending on whether they contain some words, and give hints.
 *
 * The mistakes are explained in the trace (note()) and in the text of the errors: a malformed XML or a stanza sent before the
 * authentication ends the stream with a stream error, as real servers do, while the stanzas sent to an unknown room or with a
 * wrong type get an error stanza. The texts are sent in ASCII. The stanzas can be cut in packets, and the server may send a space
 * between its stanzas (as the whitespace keep-alives of the real servers), to make the framing by the nesting matter.
 */
public class XmppServer extends ProgramPeer {
  public static final String DOMAIN     = "buggleinc.lan";
  public static final String CONFERENCE = "conference." + DOMAIN;
  private static final String STREAMS   = "http://etherx.jabber.org/streams";
  private static final String SASL      = "urn:ietf:params:xml:ns:xmpp-sasl";
  private static final String STANZAS   = "urn:ietf:params:xml:ns:xmpp-stanzas";
  private static final String MUC_USER  = "http://jabber.org/protocol/muc#user";
  /** A message on a room addressed to someone, as in "Karel: hello" or "Karel, hello" */
  private static final Pattern ADDRESSED = Pattern.compile("\\s*([^\\s:,]+)\\s*[:,]\\s*(.*)", Pattern.DOTALL);

  /** A room, with its subject, its simulated occupants, and the lines of its history (by occupant) */
  private record Room(String subject, List<String> nicks, List<String[]> history) {}
  /**
   * A line of an occupant, some seconds after a client joined, or after it first wrote to another occupant (after), possibly
   * addressed to this client, with its payload after the body
   */
  private record Said(int delay, String room, String nick, String text, boolean addressed, Payload payload, String after) {}
  /** What follows the body of a line: its XHTML-IM version, or a message forwarded from another occupant at that time */
  private record Payload(boolean xhtml, String author, String quoted, String stamp) {}
  /** The hints given on a room by a simulated occupant, about how to get the answer of another one */
  private record Hints(String room, String nick, List<String> texts) {}
  /** The answers of an occupant to the messages containing all the words (each in one of its forms), and to the other ones */
  private record Answers(List<List<String>> words, String answer, String otherwise) {}
  /** A stanza waiting for its time, sent to the client if it is then in the condition */
  private record Pending(int at, int fd, String stanza, Predicate<Client> condition) {}
  /** An address: local@domain/resource, the local part and the resource being null when absent */
  private record Jid(String local, String domain, String resource) {
    static Jid of(String jid)
    {
      int slash   = jid.indexOf('/');
      String bare = slash < 0 ? jid : jid.substring(0, slash);
      int at      = bare.indexOf('@');
      return new Jid(at < 0 ? null : bare.substring(0, at), bare.substring(at + 1), slash < 0 ? null : jid.substring(slash + 1));
    }
  }

  private enum Stage { OPENING, AUTHENTICATING, AUTHENTICATED }

  /** A connected client, by descriptor */
  private static final class Client {
    Stage stage = Stage.OPENING;
    String user;
    /** The rooms joined, with the nickname of the client in each */
    final Map<String, String> rooms = new LinkedHashMap<>();
    /** The occupants to whom the client wrote, those who gave it their answer, and its failures with each */
    final Set<String> told              = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    final Set<String> answered          = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    final Map<String, Integer> failures = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    Client() {}

    Client(Client other)
    {
      stage = other.stage;
      user  = other.user;
      rooms.putAll(other.rooms);
      told.addAll(other.told);
      answered.addAll(other.answered);
      failures.putAll(other.failures);
    }

    String jid() { return user + "@" + DOMAIN; }
  }

  private final int port;
  private int packetSize;
  private boolean keepAlives;
  private char quote = '\'';
  private int master;
  /** How many streams the server opened, numbering their id */
  private int streams;
  private final Map<String, String> accounts   = new TreeMap<>();
  private final Map<String, Room> rooms        = new LinkedHashMap<>();
  private final List<Said> said                = new ArrayList<>();
  private final Map<String, Answers> answers   = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private final Map<String, List<Hints>> hints = new TreeMap<>(String.CASE_INSENSITIVE_ORDER); // by the occupant they are about
  private final List<Pending> pending          = new ArrayList<>();
  private final Map<Integer, Client> clients   = new TreeMap<>();

  public XmppServer(String ip, int port)
  {
    super(ip, "XMPP server");
    this.port = port;
  }

  private XmppServer(XmppServer other, Map<FileDescription, FileDescription> copies)
  {
    super(other, copies);
    port       = other.port;
    packetSize = other.packetSize;
    keepAlives = other.keepAlives;
    quote      = other.quote;
    master     = other.master;
    streams    = other.streams;
    accounts.putAll(other.accounts);
    rooms.putAll(other.rooms); // the scenario is never changed once given
    said.addAll(other.said);
    answers.putAll(other.answers);
    hints.putAll(other.hints);
    pending.addAll(other.pending);
    other.clients.forEach((fd, client) -> clients.put(fd, new Client(client)));
  }

  @Override protected XmppServer copy(Map<FileDescription, FileDescription> copies) { return new XmppServer(this, copies); }

  // ---------------------------------------------------------------- the scenario, given by the exercise

  /** Adds an account of the domain, with its password */
  public XmppServer account(String user, String password)
  {
    accounts.put(user, password);
    return this;
  }

  /** Adds a room of conference.buggleinc.lan (such as "robots"), with its subject and its simulated occupants */
  public XmppServer room(String name, String subject, String... nicks)
  {
    rooms.put(name, new Room(ascii(subject), List.of(nicks), new ArrayList<>()));
    return this;
  }

  /** Adds a line said by an occupant before the client joined, sent in the history of the room (a minute after the previous one) */
  public XmppServer history(String room, String nick, String text)
  {
    rooms.get(room).history().add(new String[] {nick, ascii(text)});
    return this;
  }

  /** Makes an occupant say a line on a room, that many seconds after a client joined it */
  public XmppServer say(int delay, String room, String nick, String text) { return line(delay, room, nick, text, false, null); }

  /** Makes an occupant say a line on a room, followed by its XHTML-IM version (XEP-0071), whose <html> holds a <body> of its own */
  public XmppServer sayInXhtml(int delay, String room, String nick, String text)
  {
    return line(delay, room, nick, text, false, new Payload(true, null, null, null));
  }

  /**
   * Makes an occupant say a line on a room, forwarding (XEP-0297) a message that another occupant said there at that time (such
   * as "2023-12-24T18:00:00Z"): a <message> inside the <message>
   */
  public XmppServer forward(int delay, String room, String nick, String text, String author, String quoted, String stamp)
  {
    return line(delay, room, nick, text, false, new Payload(false, author, ascii(quoted), stamp));
  }

  /**
   * Makes an occupant address the client on a room, that many seconds after it joined, unless the client wrote to this occupant
   * before. The text is a MessageFormat pattern, whose {0} is the nickname of the client.
   */
  public XmppServer address(int delay, String room, String nick, String text) { return line(delay, room, nick, text, true, null); }

  private XmppServer line(int delay, String room, String nick, String text, boolean addressed, Payload payload)
  {
    said.add(new Said(delay, room, nick, ascii(text), addressed, payload, null));
    return this;
  }

  /**
   * Says the last line given when a client first writes to that occupant, its delay counting from then, rather than when it
   * joined: the line comes before the answer of the occupant if its delay is 0
   */
  public XmppServer afterWritingTo(String occupant)
  {
    Said last = said.remove(said.size() - 1);
    said.add(new Said(last.delay(), last.room(), last.nick(), last.text(), last.addressed(), last.payload(), occupant));
    return this;
  }

  /**
   * Makes an occupant give hints on a room to the client who wrote to another occupant without getting its answer: the next hint
   * after each failure, two seconds after the reply, the last one being repeated. The hints are MessageFormat patterns, whose {0}
   * is the nickname of the client, and {1}, {2}... the words that the other occupant expects (in their first form).
   */
  public XmppServer hints(String room, String nick, String about, String... texts)
  {
    hints.computeIfAbsent(about, occupant -> new ArrayList<>()).add(new Hints(room, nick, Stream.of(texts).map(ProgramPeer::ascii).toList()));
    return this;
  }

  /**
   * Makes an occupant answer one second later: answer to the messages containing all the words, and otherwise to the other ones.
   * Each word is given with its accepted forms, whatever their case and accents. It answers in private to the private messages,
   * and on the room to the messages addressed to it there (as in "Karel: hello"), addressing the client in turn.
   */
  public XmppServer answer(String nick, List<List<String>> words, String answer, String otherwise)
  {
    List<List<String>> forms = words.stream().map(word -> word.stream().map(form -> ascii(form).toLowerCase(Locale.ROOT)).toList()).toList();
    answers.put(nick, new Answers(forms, ascii(answer), ascii(otherwise)));
    return this;
  }

  /** Cuts everything the server sends in packets of that size, one per second */
  public XmppServer packets(int size)
  {
    packetSize = size;
    return this;
  }

  /** Sends a single space after each line of the occupants, as the whitespace keep-alives of the real servers */
  public XmppServer keepAlives()
  {
    keepAlives = true;
    return this;
  }

  /** Quotes the attributes with ", instead of ' as most servers */
  public XmppServer doubleQuotes()
  {
    quote = '"';
    return this;
  }

  // ---------------------------------------------------------------- the event loop

  @Override protected void start() { master = listen(port); }

  @Override protected boolean act()
  {
    int fd = accept(master);
    if (fd >= 0) { // the client speaks first, with the header of its stream
      clients.put(fd, new Client());
      return true;
    }
    for (Map.Entry<Integer, Client> entry : clients.entrySet()) {
      fd            = entry.getKey();
      Client client = entry.getValue();
      String data   = peek(fd);
      try {
        Xml.Unit unit = Xml.next(data, client.stage == Stage.OPENING, client.stage == Stage.AUTHENTICATED);
        if (unit != null) {
          receive(fd, unit.length());
          if (unit.header() != null)
            open(fd, client, unit.header());
          else if (unit.end())
            end(fd);
          else
            stanza(fd, client, unit.element());
          return true;
        }
      } catch (Xml.StreamError e) {
        receive(fd, data.length());
        streamError(fd, client, e.condition, e.getMessage());
        return true;
      }
      if (ended(fd)) { // the client left without ending its stream
        if (!data.isEmpty())
          receive(fd, data.length());
        note(Game.i18n.tr("The client closed its connection without ending its stream with </stream:stream>."));
        close(fd);
        clients.remove(fd);
        return true;
      }
    }
    for (Pending stanza : pending) // in the order they were planned
      if (stanza.at() <= now()) {
        pending.remove(stanza);
        if (clients.containsKey(stanza.fd()) && stanza.condition().test(clients.get(stanza.fd())))
          out(stanza.fd(), stanza.stanza() + (keepAlives ? " " : ""));
        return true;
      }
    return false;
  }

  @Override protected int timer() { return pending.stream().mapToInt(Pending::at).min().orElse(-1); }

  @Override protected String waiting() { return clients.isEmpty() ? "waiting for a connection" : "waiting for a stanza"; }

  /** Sends that data to a client, in packets if asked */
  private void out(int fd, String data)
  {
    if (packetSize > 0)
      sendInPackets(fd, data, packetSize, 1);
    else
      send(fd, data);
  }

  /** An attribute, with a space before it */
  private String attribute(String name, String value) { return " " + name + "=" + quote + Xml.escape(value, quote) + quote; }

  /** The header of the stream of the server, after its XML declaration */
  private String header()
  {
    streams++;
    return "<?xml version=" + quote + "1.0" + quote + "?><stream:stream" + attribute("xmlns", "jabber:client") + attribute("xmlns:stream", STREAMS) +
        attribute("id", "plm" + streams) + attribute("from", DOMAIN) + attribute("version", "1.0") + attribute("xml:lang", "en") + ">";
  }

  /** A text of the server or of an occupant, in its element with that namespace */
  private String text(String namespace, String text)
  {
    return "<text" + attribute("xmlns", namespace) + attribute("xml:lang", "en") + ">" + Xml.escape(ascii(text), (char)0) + "</text>";
  }

  /** Ends the stream of a client with that error, explained in the trace and in the error, and closes the connection */
  private void streamError(int fd, Client client, String condition, String explanation)
  {
    note(explanation);
    String opening = client.stage == Stage.OPENING ? header() : ""; // a server always opens its stream before an error
    out(fd, opening + "<stream:error><" + condition + attribute("xmlns", "urn:ietf:params:xml:ns:xmpp-streams") + "/>" +
                text("urn:ietf:params:xml:ns:xmpp-streams", explanation) + "</stream:error></stream:stream>");
    close(fd);
    clients.remove(fd);
  }

  /** Answers a stanza with an error stanza of that type and condition (of urn:ietf:params:xml:ns:xmpp-stanzas), explained */
  private void stanzaError(int fd, Client client, Xml.Element stanza, String type, String condition, String explanation)
  {
    note(explanation);
    String id = stanza.attribute("id");
    out(fd, "<" + stanza.name() + attribute("from", stanza.attributes().getOrDefault("to", DOMAIN)) + attribute("to", client.jid()) +
                attribute("type", "error") + (id == null ? "" : attribute("id", id)) + "><error" + attribute("type", type) + "><" + condition +
                attribute("xmlns", STANZAS) + "/>" + text(STANZAS, explanation) + "</error></" + stanza.name() + ">");
  }

  // ---------------------------------------------------------------- the stream and the authentication

  /** Opens the stream of the server on the header of the client, or restarts it after the authentication */
  private void open(int fd, Client client, Xml.Element header)
  {
    String to = header.attribute("to");
    if (!STREAMS.equals(header.attribute("xmlns:stream")))
      streamError(fd, client, "invalid-namespace", Game.i18n.tr("The header of the stream declares the prefix stream with xmlns:stream=''{0}''.", STREAMS));
    else if (!"jabber:client".equals(header.attribute("xmlns")))
      streamError(fd, client, "invalid-namespace", Game.i18n.tr("The header of the stream of a client declares xmlns='jabber:client'."));
    else if (!DOMAIN.equals(to))
      streamError(fd, client, "host-unknown",
                  to == null ? Game.i18n.tr("The header of the stream names the domain of the server in its attribute to, here to=''{0}''.", DOMAIN)
                             : Game.i18n.tr("This server serves the domain {0}, not {1}.", DOMAIN, to));
    else if (!"1.0".equals(header.attribute("version")))
      streamError(fd, client, "unsupported-version", Game.i18n.tr("The header of the stream asks for the version 1.0 of XMPP, with version='1.0'."));
    else if (client.stage == Stage.OPENING) {
      client.stage = Stage.AUTHENTICATING;
      out(fd, header() + "<stream:features><mechanisms" + attribute("xmlns", SASL) + "><mechanism>PLAIN</mechanism></mechanisms></stream:features>");
    } else
      out(fd, header() + "<stream:features/>"); // a restart, after which a real server offers to bind a resource
  }

  /** Ends the stream of a client on its request, as the server does too */
  private void end(int fd)
  {
    out(fd, "</stream:stream>");
    close(fd);
    clients.remove(fd);
  }

  private void stanza(int fd, Client client, Xml.Element stanza)
  {
    switch (stanza.name()) {
      case "auth" -> auth(fd, client, stanza);
      case "message", "presence", "iq" -> {
        if (client.stage != Stage.AUTHENTICATED)
          streamError(fd, client, "not-authorized", Game.i18n.tr("The client must authenticate with <auth> before sending any stanza."));
        else if (stanza.name().equals("message"))
          message(fd, client, stanza);
        else if (stanza.name().equals("presence"))
          presence(fd, client, stanza);
        else
          iq(fd, client, stanza);
      }
      default ->
        streamError(fd, client, "unsupported-stanza-type",
                    Game.i18n.tr("The element <{0}> is unknown at the top of a stream, where the stanzas are <message>, <presence> and <iq>.", stanza.name()));
    }
  }

  /** Answers a SASL authentication (RFC 4616 for PLAIN): its success, or its failure with the condition and its explanation */
  private void auth(int fd, Client client, Xml.Element auth)
  {
    if (!SASL.equals(auth.attribute("xmlns"))) {
      streamError(fd, client, "unsupported-stanza-type", Game.i18n.tr("The element <auth> of SASL declares xmlns=''{0}''.", SASL));
      return;
    }
    if (client.stage == Stage.AUTHENTICATED) {
      streamError(fd, client, "policy-violation", Game.i18n.tr("The client is already authenticated."));
      return;
    }
    String failure = null, explanation = null;
    if (!"PLAIN".equals(auth.attribute("mechanism"))) {
      failure     = "invalid-mechanism";
      explanation = Game.i18n.tr("This server only offers the mechanism PLAIN, given as mechanism='PLAIN'.");
    } else {
      String credentials = null;
      try {
        credentials = new String(Base64.getDecoder().decode(auth.text().strip()), StandardCharsets.ISO_8859_1);
      } catch (IllegalArgumentException e) {
        failure     = "incorrect-encoding";
        explanation = Game.i18n.tr("The content of <auth> is not in base64: only letters, digits, + and / in groups of four characters, the last group "
                                   + "being completed by =.");
      }
      if (credentials != null && credentials.isEmpty()) {
        failure     = "malformed-request";
        explanation = Game.i18n.tr("The element <auth> holds no credentials: the base64 of a zero byte, the user name, a zero byte and the password.");
      } else if (credentials != null) {
        note(Game.i18n.tr("The credentials decoded from base64 are {0}.", PosixWorld.quoted(credentials)));
        String[] parts = credentials.split("\0", -1);
        String user    = parts.length == 3 ? parts[1].replaceFirst("@" + Pattern.quote(DOMAIN) + "$", "") : null;
        if (parts.length != 3) {
          failure     = "malformed-request";
          explanation = Game.i18n.tr("The credentials hold {0} zero bytes, where PLAIN separates the user name and the password with two of them: "
                                         + "a zero byte, the user name, a zero byte and the password.",
                                     String.valueOf(parts.length - 1));
        } else if (!parts[0].isEmpty() && !parts[0].equals(user + "@" + DOMAIN)) {
          failure     = "invalid-authzid";
          explanation = Game.i18n.tr("The credentials start with {0} before their first zero byte, where nothing is expected.", PosixWorld.quoted(parts[0]));
        } else if (!parts[2].equals(accounts.get(user))) {
          failure     = "not-authorized";
          explanation = Game.i18n.tr("Wrong user name or password.");
        } else
          client.user = user;
      }
    }
    if (failure != null) {
      note(explanation);
      out(fd, "<failure" + attribute("xmlns", SASL) + "><" + failure + "/>" + text(SASL, explanation) + "</failure>");
    } else {
      client.stage = Stage.AUTHENTICATED;
      out(fd, "<success" + attribute("xmlns", SASL) + "/>");
    }
  }

  // ---------------------------------------------------------------- the stanzas

  private void iq(int fd, Client client, Xml.Element iq)
  {
    String type = iq.attribute("type");
    if ("get".equals(type) || "set".equals(type))
      stanzaError(fd, client, iq, "cancel", "service-unavailable",
                  Game.i18n.tr("This server answers no request <iq>: it only carries presences and messages."));
  }

  /** The address of an occupant in a room */
  private static String occupant(String room, String nick) { return room + "@" + CONFERENCE + "/" + nick; }

  /** The presence of an occupant of a room to the client, with its affiliation and its role, and the status codes given */
  private String presence(Client client, String room, String nick, String affiliation, String role, String type, String... codes)
  {
    StringBuilder res = new StringBuilder("<presence" + attribute("from", occupant(room, nick)) + attribute("to", client.jid()) +
                                          (type == null ? "" : attribute("type", type)) + "><x" + attribute("xmlns", MUC_USER) + "><item" +
                                          attribute("affiliation", affiliation) + attribute("role", role) + "/>");
    for (String code : codes)
      res.append("<status").append(attribute("code", code)).append("/>");
    return res + "</x></presence>";
  }

  /** A message of an occupant to the client, on the room or in private, with that payload after its body */
  private String message(Client client, String room, String nick, String type, String id, String body, String payload)
  {
    return "<message" + attribute("from", occupant(room, nick)) + attribute("to", client.jid()) + attribute("type", type) +
        (id == null ? "" : attribute("id", id)) + "><body>" + Xml.escape(body, (char)0) + "</body>" + payload + "</message>";
  }

  /** Joins or leaves a room */
  private void presence(int fd, Client client, Xml.Element presence)
  {
    String to = presence.attribute("to");
    if (to == null) { // the initial presence of the client, for its contacts
      note(Game.i18n.tr("This presence has no address: it tells the contacts of the client that it is available, but it has none here."));
      return;
    }
    Jid jid   = Jid.of(to);
    Room room = jid.domain().equals(CONFERENCE) ? rooms.get(jid.local()) : null;
    if (room == null) {
      stanzaError(fd, client, presence, "cancel", "item-not-found",
                  jid.domain().equals(CONFERENCE) ? Game.i18n.tr("There is no room {0} on {1}.", String.valueOf(jid.local()), CONFERENCE)
                                                  : Game.i18n.tr("The rooms are on {0}, as name@{0}/nickname.", CONFERENCE));
      return;
    }
    if ("unavailable".equals(presence.attribute("type"))) {
      String nick = client.rooms.remove(jid.local());
      if (nick != null)
        out(fd, presence(client, jid.local(), nick, "none", "none", "unavailable", "110"));
      return;
    }
    if (jid.resource() == null || jid.resource().isEmpty()) {
      stanzaError(fd, client, presence, "modify", "jid-malformed",
                  Game.i18n.tr("To join a room, the presence goes to name@{0}/nickname, with the nickname of the client.", CONFERENCE));
      return;
    }
    if (room.nicks().contains(jid.resource())) {
      stanzaError(fd, client, presence, "cancel", "conflict", Game.i18n.tr("The nickname {0} is already taken in this room.", jid.resource()));
      return;
    }
    if (client.rooms.containsKey(jid.local()))
      return; // already there
    client.rooms.put(jid.local(), jid.resource());
    StringBuilder burst = new StringBuilder();
    for (String nick : room.nicks())
      burst.append(presence(client, jid.local(), nick, "member", "participant", null));
    burst.append(presence(client, jid.local(), jid.resource(), "none", "participant", null, "110")); // 110: the presence of the client
    for (int i = 0; i < room.history().size(); i++) {
      String stamp = String.format("2023-12-31T23:%02d:00Z", 60 - room.history().size() + i);
      String delay = "<delay" + attribute("xmlns", "urn:xmpp:delay") + attribute("from", jid.local() + "@" + CONFERENCE) + attribute("stamp", stamp) + "/>";
      burst.append(message(client, jid.local(), room.history().get(i)[0], "groupchat", null, room.history().get(i)[1], delay));
    }
    burst.append("<message" + attribute("from", jid.local() + "@" + CONFERENCE) + attribute("to", client.jid()) + attribute("type", "groupchat") +
                 "><subject>" + Xml.escape(room.subject(), (char)0) + "</subject></message>");
    out(fd, burst.toString());
    for (Said line : said) // the lines of the occupants, for this client
      if (line.room().equals(jid.local()) && line.after() == null)
        plan(client, fd, line);
  }

  /** Plans a line of an occupant to the client, said after its delay if the client is still in the room */
  private void plan(Client client, int fd, Said line)
  {
    String text = line.addressed() ? MessageFormat.format(line.text(), client.rooms.get(line.room())) : line.text();
    plan(now() + line.delay(), fd, message(client, line.room(), line.nick(), "groupchat", null, text, payload(line, text)),
         c -> c.rooms.containsKey(line.room()) && !(line.addressed() && c.told.contains(line.nick())));
  }

  /** What follows the body of a line of an occupant */
  private String payload(Said line, String text)
  {
    if (line.payload() == null)
      return "";
    if (line.payload().xhtml())
      return "<html" + attribute("xmlns", "http://jabber.org/protocol/xhtml-im") + "><body" + attribute("xmlns", "http://www.w3.org/1999/xhtml") + "><p>" +
          Xml.escape(text, (char)0) + "</p></body></html>";
    return "<forwarded" + attribute("xmlns", "urn:xmpp:forward:0") + "><delay" + attribute("xmlns", "urn:xmpp:delay") +
        attribute("stamp", line.payload().stamp()) + "/><message" + attribute("xmlns", "jabber:client") +
        attribute("from", occupant(line.room(), line.payload().author())) + attribute("type", "groupchat") + "><body>" +
        Xml.escape(line.payload().quoted(), (char)0) + "</body></message></forwarded>";
  }

  /** Plans a stanza to a client, sent at that time if the client is then in the condition */
  private void plan(int at, int fd, String stanza, Predicate<Client> condition)
  {
    pending.add(new Pending(at, fd, stanza, condition));
    pending.sort((a, b) -> Integer.compare(a.at(), b.at()));
  }

  /** Sends a message to a room, or in private to an occupant */
  private void message(int fd, Client client, Xml.Element message)
  {
    String to   = message.attribute("to");
    String type = message.attributes().getOrDefault("type", "normal");
    if ("error".equals(type))
      return;
    Jid jid   = to == null ? null : Jid.of(to);
    Room room = jid != null && jid.domain().equals(CONFERENCE) ? rooms.get(jid.local()) : null;
    if (room == null) {
      stanzaError(fd, client, message, "cancel", "item-not-found",
                  jid != null && jid.domain().equals(CONFERENCE) ? Game.i18n.tr("There is no room {0} on {1}.", String.valueOf(jid.local()), CONFERENCE)
                                                                 : Game.i18n.tr("Nobody receives this message: the rooms are on {0}.", CONFERENCE));
      return;
    }
    String nick = client.rooms.get(jid.local());
    if (nick == null) {
      stanzaError(fd, client, message, "cancel", "not-acceptable", Game.i18n.tr("Only the occupants of a room may send messages there: join it first."));
      return;
    }
    Xml.Element body = message.child("body");
    if (jid.resource() == null) { // to the room, which sends it to every occupant, including its sender
      if (!type.equals("groupchat"))
        stanzaError(fd, client, message, "modify", "bad-request", Game.i18n.tr("A message to a room has the type groupchat, not {0}.", type));
      else if (body != null) {
        out(fd, message(client, jid.local(), nick, "groupchat", message.attribute("id"), body.text(), ""));
        Matcher addressed = ADDRESSED.matcher(body.text());
        if (addressed.matches())
          room.nicks()
              .stream()
              .filter(addressed.group(1)::equalsIgnoreCase)
              .findFirst()
              .ifPresent(occupant -> ask(fd, client, jid.local(), occupant, addressed.group(2), false));
      }
    } else if (!room.nicks().contains(jid.resource()))
      stanzaError(fd, client, message, "cancel", "item-not-found", Game.i18n.tr("There is nobody named {0} in this room.", jid.resource()));
    else if (type.equals("groupchat"))
      stanzaError(fd, client, message, "modify", "bad-request", Game.i18n.tr("A private message to an occupant has the type chat, not groupchat."));
    else if (body != null)
      ask(fd, client, jid.local(), jid.resource(), body.text(), true);
  }

  /** Makes an occupant react to a message of the client: in private, or addressed to it on the room, where it addresses the client */
  private void ask(int fd, Client client, String room, String occupant, String text, boolean privately)
  {
    if (client.told.add(occupant))
      for (Said line : said)
        if (line.room().equals(room) && occupant.equalsIgnoreCase(line.after()))
          plan(client, fd, line);
    Answers answer = answers.get(occupant);
    if (answer == null)
      return;
    String nick       = client.rooms.get(room);
    String normalized = ascii(PosixWorld.text(text)).toLowerCase(Locale.ROOT); // the accents of the client's UTF-8 removed too
    boolean matches   = answer.words().stream().allMatch(forms -> forms.stream().anyMatch(normalized::contains));
    String reply      = matches ? answer.answer() : answer.otherwise();
    String muc        = "<x" + attribute("xmlns", MUC_USER) + "/>"; // marks a private message coming from a room
    plan(now() + 1, fd,
         privately ? message(client, room, occupant, "chat", null, reply, muc) : message(client, room, occupant, "groupchat", null, nick + ": " + reply, ""),
         c -> c.rooms.containsKey(room));
    if (matches) {
      client.answered.add(occupant);
      return;
    }
    int failures   = client.failures.merge(occupant, 1, Integer::sum);
    Object[] words = Stream.concat(Stream.of(nick), answer.words().stream().map(forms -> forms.get(0))).toArray();
    for (Hints hint : hints.getOrDefault(occupant, List.of())) {
      String hinted = MessageFormat.format(hint.texts().get(Math.min(failures, hint.texts().size()) - 1), words);
      plan(now() + 3, fd, message(client, hint.room(), hint.nick(), "groupchat", null, hinted, ""), // two seconds after the reply
           c -> c.rooms.containsKey(hint.room()) && !c.answered.contains(occupant));
    }
  }

  @Override String describe()
  {
    StringBuilder sb = new StringBuilder(super.describe());
    for (Client client : clients.values()) {
      sb.append(", client ").append(client.user == null ? "not authenticated" : client.jid());
      client.rooms.forEach((room, nick) -> sb.append(" in ").append(occupant(room, nick)));
    }
    return sb.toString();
  }
}
