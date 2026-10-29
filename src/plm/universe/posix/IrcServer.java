package plm.universe.posix;

import java.text.MessageFormat;
import java.util.ArrayList;
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
 * An IRC server (RFC 2812), exact to the byte as the usual servers. It greets each new client with a NOTICE telling how to
 * register, as many servers do (so that a client receiving first gets something). After registering with NICK and USER, the
 * clients can JOIN and PART channels, send PRIVMSG to a channel or to someone, ask the NAMES of a channel, PING the server and QUIT. The errors
 * are the usual numeric replies (421 unknown command, 451 not registered, 433 nickname in use, 461 not enough parameters...). As
 * most servers, it splits the lines on \n and ignores a \r before it, and the commands are case-insensitive.
 *
 * The other members of the channels are simulated: they say their lines at given times after a client joined the channel (each
 * client receives the whole chat, whenever it joins), and answer a private message after a second, depending on whether it
 * contains some words. They also answer on a channel the messages addressed to them, as in "Bob: hello". A member may also
 * address the client who did not write to it yet, or give hints to the client who wrote to another member without getting its
 * answer. The texts are converted to ASCII, which the world only accepts for now (the translated texts lose their
 * accents). The lines of the members can be cut in packets, so that the framing of the lines matters.
 */
public class IrcServer extends ProgramPeer {
  public static final String NAME     = "irc.buggleinc.com";
  /** A message on a channel addressed to someone, as in "Bob: hello" or "Bob, hello" */
  private static final Pattern ADDRESSED = Pattern.compile("\\s*([^\\s:,]+)\\s*[:,]\\s*(.*)");
  private static final String VERSION = "plm-ircd-1.0";

  /** A line said on a channel by a simulated member, some seconds after a client joined, to this client if addressed */
  private record Said(int delay, String channel, String nick, String text, boolean addressed) {}
  /** The hints given on a channel by a simulated member, about how to get the answer of another member */
  private record Hints(String channel, String nick, List<String> texts) {}
  /** The answers of a simulated member to the messages containing all the words (each in one of its forms), and to the other ones */
  private record Answers(List<List<String>> words, String answer, String otherwise) {}
  /**
   * A line of a simulated member to a client, waiting for its time (what it says on a channel, or tells in private), said if the
   * client is then in the condition
   */
  private record Pending(int at, int fd, String line, Predicate<Client> condition) {}

  /** A connected client, by descriptor */
  private static final class Client {
    String nick, user;
    boolean registered;
    final List<String> channels = new ArrayList<>();
    /** The simulated members to which the client wrote in private, those who gave it their answer, and its failures with each */
    final Set<String> told              = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    final Set<String> answered          = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    final Map<String, Integer> failures = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    Client() {}

    Client(Client other)
    {
      nick       = other.nick;
      user       = other.user;
      registered = other.registered;
      channels.addAll(other.channels);
      told.addAll(other.told);
      answered.addAll(other.answered);
      failures.putAll(other.failures);
    }

    String prefix() { return nick + "!" + user + "@" + PosixWorld.STUDENT_IP; }
  }

  private final int port;
  private int packetSize;
  private int master;
  /** The channels with their topic, and their simulated members */
  private final Map<String, String> topics        = new LinkedHashMap<>();
  private final Map<String, List<String>> members = new LinkedHashMap<>();
  private final List<Said> said                   = new ArrayList<>();
  private final Map<String, Answers> answers      = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  private final Map<String, List<Hints>> hints    = new TreeMap<>(String.CASE_INSENSITIVE_ORDER); // by the member they are about
  private final List<Pending> pending             = new ArrayList<>();
  private final Map<Integer, Client> clients      = new TreeMap<>();

  public IrcServer(String ip, int port)
  {
    super(ip, "IRC server");
    this.port = port;
  }

  private IrcServer(IrcServer other, Map<FileDescription, FileDescription> copies)
  {
    super(other, copies);
    port       = other.port;
    packetSize = other.packetSize;
    master     = other.master;
    topics.putAll(other.topics);
    other.members.forEach((channel, nicks) -> members.put(channel, new ArrayList<>(nicks)));
    said.addAll(other.said);
    answers.putAll(other.answers);
    hints.putAll(other.hints); // the lists are never changed once the scenario is given
    pending.addAll(other.pending);
    other.clients.forEach((fd, client) -> clients.put(fd, new Client(client)));
  }

  @Override protected IrcServer copy(Map<FileDescription, FileDescription> copies) { return new IrcServer(this, copies); }

  // ---------------------------------------------------------------- the scenario, given by the exercise

  /** Adds a channel (whose name starts with #), with its topic and its simulated members */
  public IrcServer channel(String name, String topic, String... nicks)
  {
    topics.put(name, ascii(topic));
    members.put(name, new ArrayList<>(List.of(nicks)));
    return this;
  }

  /** Makes a simulated member say a line on a channel, that many seconds after a client joined it */
  public IrcServer say(int delay, String channel, String nick, String text)
  {
    said.add(new Said(delay, channel, nick, ascii(text), false));
    return this;
  }

  /**
   * Makes a simulated member address the client on a channel, that many seconds after it joined, unless the client wrote to this
   * member in private before. The text is a MessageFormat pattern, whose {0} is the nickname of the client.
   */
  public IrcServer address(int delay, String channel, String nick, String text)
  {
    said.add(new Said(delay, channel, nick, ascii(text), true));
    return this;
  }

  /**
   * Makes a simulated member give hints on a channel to the client who wrote in private to another member without getting its
   * answer: the next hint after each failure, two seconds after the reply, the last one being repeated, and none once the answer
   * was given. The hints are MessageFormat patterns, whose {0} is the nickname of the client, and {1}, {2}... the words that the
   * other member expects (in their first form, see answer()), so that a hint can tell an exact sentence, consistent with the
   * translated words.
   */
  public IrcServer hints(String channel, String nick, String about, String... texts)
  {
    hints.computeIfAbsent(about, member -> new ArrayList<>()).add(new Hints(channel, nick, Stream.of(texts).map(ProgramPeer::ascii).toList()));
    return this;
  }

  /**
   * Makes a simulated member answer one second later: answer to the messages containing all the words, and otherwise to the other
   * ones. Each word is given with its accepted forms (such as its translation and the English word), whatever their case and
   * accents. It answers in private to the private messages, and on the channel to the messages addressed to it there (as in "Bob:
   * hello"), addressing the client in turn
   */
  public IrcServer answer(String nick, List<List<String>> words, String answer, String otherwise)
  {
    List<List<String>> forms = words.stream().map(word -> word.stream().map(form -> ascii(form).toLowerCase(Locale.ROOT)).toList()).toList();
    answers.put(nick, new Answers(forms, ascii(answer), ascii(otherwise)));
    return this;
  }

  /** Cuts the lines of the simulated members in packets of that size, one per second */
  public IrcServer packets(int size)
  {
    packetSize = size;
    return this;
  }

  // ---------------------------------------------------------------- the event loop

  @Override protected void start() { master = listen(port); }

  @Override protected boolean act()
  {
    int fd = accept(master);
    if (fd >= 0) {
      clients.put(fd, new Client());
      String greeting = Game.i18n.tr("Welcome! Please register with the NICK and USER commands (see the world documentation for details).");
      out(fd, ":" + NAME + " NOTICE * :*** " + ascii(greeting));
      return true;
    }
    for (Map.Entry<Integer, Client> entry : clients.entrySet()) {
      String line = receive(entry.getKey(), "\n");
      if (line != null) {
        handle(entry.getKey(), entry.getValue(), line.endsWith("\r") ? line.substring(0, line.length() - 1) : line);
        return true;
      }
      if (ended(entry.getKey())) { // the client left without QUIT
        close(entry.getKey());
        clients.remove(entry.getKey());
        return true;
      }
    }
    for (Pending line : pending) // in the order they were planned
      if (line.at() <= now()) {
        pending.remove(line);
        if (clients.containsKey(line.fd()) && line.condition().test(clients.get(line.fd()))) {
          if (packetSize > 0)
            sendInPackets(line.fd(), line.line() + "\r\n", packetSize, 1);
          else
            send(line.fd(), line.line() + "\r\n");
        }
        return true;
      }
    return false;
  }

  @Override protected int timer() { return pending.stream().mapToInt(Pending::at).min().orElse(-1); }

  @Override protected String waiting() { return clients.isEmpty() ? "waiting for a connection" : "waiting for a command"; }

  /** The prefix of a simulated member */
  private static String member(String nick) { return nick + "!" + nick.toLowerCase(Locale.ROOT) + "@buggleinc.com"; }

  /** Sends one or several lines (separated by \r\n) of the server to a client */
  private void out(int fd, String lines) { send(fd, lines + "\r\n"); }

  /** Sends a numeric reply of the server, whose target is the nick of the client (or * before it has one) */
  private void numeric(int fd, Client client, String code, String text)
  {
    out(fd, ":" + NAME + " " + code + " " + (client.nick == null ? "*" : client.nick) + " " + text);
  }

  // ---------------------------------------------------------------- the commands

  /** The parameters of a line after its command: words separated by spaces, the last one starting with : taking the rest */
  private static List<String> parameters(String rest)
  {
    List<String> res = new ArrayList<>();
    while (!rest.isEmpty()) {
      if (rest.startsWith(":")) {
        res.add(rest.substring(1));
        break;
      }
      int space = rest.indexOf(' ');
      res.add(space < 0 ? rest : rest.substring(0, space));
      rest = space < 0 ? "" : rest.substring(space + 1).stripLeading();
    }
    return res;
  }

  private void handle(int fd, Client client, String line)
  {
    if (line.startsWith(":")) // the prefix that a client may give is ignored
      line = line.contains(" ") ? line.substring(line.indexOf(' ') + 1) : "";
    line = line.stripLeading();
    if (line.isEmpty())
      return;
    int space           = line.indexOf(' ');
    String command      = (space < 0 ? line : line.substring(0, space)).toUpperCase(Locale.ROOT);
    List<String> params = parameters(space < 0 ? "" : line.substring(space + 1).stripLeading());
    switch (command) {
      case "NICK" -> nick(fd, client, params);
      case "USER" -> user(fd, client, params);
      case "QUIT" -> quit(fd, params);
      case "PING" -> {
        if (params.isEmpty())
          numeric(fd, client, "409", ":No origin specified");
        else
          out(fd, ":" + NAME + " PONG " + NAME + " :" + params.get(0));
      }
      case "PONG" -> {
      }
      default -> {
        if (!client.registered)
          numeric(fd, client, "451", ":You have not registered");
        else
          switch (command) {
            case "JOIN" -> join(fd, client, params);
            case "PART" -> part(fd, client, params);
            case "PRIVMSG" -> privmsg(fd, client, params);
            case "NAMES" -> {
              if (params.isEmpty())
                numeric(fd, client, "366", "* :End of /NAMES list.");
              else
                names(fd, client, params.get(0));
            }
            default -> numeric(fd, client, "421", command + " :Unknown command");
          }
      }
    }
  }

  private void nick(int fd, Client client, List<String> params)
  {
    if (params.isEmpty()) {
      numeric(fd, client, "431", ":No nickname given");
      return;
    }
    String nick = params.get(0);
    if (!nick.matches("[A-Za-z\\[\\]\\\\`_^{|}][A-Za-z0-9\\[\\]\\\\`_^{|}-]{0,29}")) {
      numeric(fd, client, "432", nick + " :Erroneous Nickname");
      return;
    }
    boolean taken = members.values().stream().anyMatch(nicks -> nicks.stream().anyMatch(nick::equalsIgnoreCase)) ||
                    clients.values().stream().anyMatch(other -> other != client && nick.equalsIgnoreCase(other.nick));
    if (taken) {
      numeric(fd, client, "433", nick + " :Nickname is already in use");
      return;
    }
    if (client.registered)
      out(fd, ":" + client.prefix() + " NICK :" + nick);
    client.nick = nick;
    welcome(fd, client);
  }

  private void user(int fd, Client client, List<String> params)
  {
    if (client.registered)
      numeric(fd, client, "462", ":You may not reregister");
    else if (params.size() < 4)
      numeric(fd, client, "461", "USER :Not enough parameters");
    else {
      client.user = params.get(0);
      welcome(fd, client);
    }
  }

  /** Registers the client once it gave both its nick and its user name, with the welcome burst of the server */
  private void welcome(int fd, Client client)
  {
    if (client.registered || client.nick == null || client.user == null)
      return;
    client.registered = true;
    String to         = " " + client.nick + " ";
    out(fd,
        String.join("\r\n", //
                    ":" + NAME + " 001" + to + ":Welcome to the BuggleInc IRC Network " + client.prefix(),
                    ":" + NAME + " 002" + to + ":Your host is " + NAME + ", running version " + VERSION,
                    ":" + NAME + " 003" + to + ":This server was created Mon Jan 1 2024 at 00:00:00 UTC",
                    ":" + NAME + " 004" + to + NAME + " " + VERSION + " io ntk", ":" + NAME + " 375" + to + ":- " + NAME + " Message of the day - ",
                    ":" + NAME + " 372" + to + ":- Be nice with the other buggles, and enjoy your stay!", ":" + NAME + " 376" + to + ":End of /MOTD command."));
  }

  private void quit(int fd, List<String> params)
  {
    out(fd, "ERROR :Closing Link: " + PosixWorld.STUDENT_IP + " (" + (params.isEmpty() ? "Client Quit" : "Quit: " + params.get(0)) + ")");
    close(fd);
    clients.remove(fd);
  }

  private void join(int fd, Client client, List<String> params)
  {
    if (params.isEmpty()) {
      numeric(fd, client, "461", "JOIN :Not enough parameters");
      return;
    }
    for (String channel : params.get(0).split(",")) {
      if (!channel.matches("#[^ ,\\x07]{1,49}")) {
        numeric(fd, client, "403", channel + " :No such channel");
        continue;
      }
      if (client.channels.contains(channel))
        continue;
      client.channels.add(channel);
      members.putIfAbsent(channel, new ArrayList<>()); // a new channel, without topic
      out(fd, ":" + client.prefix() + " JOIN " + channel);
      if (topics.get(channel) != null)
        numeric(fd, client, "332", channel + " :" + topics.get(channel));
      names(fd, client, channel);
      for (Said line : said) // the chat of the members, for this client
        if (line.channel().equals(channel)) {
          String text = line.addressed() ? MessageFormat.format(line.text(), client.nick) : line.text();
          plan(now() + line.delay(), fd, ":" + member(line.nick()) + " PRIVMSG " + channel + " :" + text,
               line.addressed() ? c -> !c.told.contains(line.nick()) : c -> true);
        }
    }
  }

  /** Plans a line of a simulated member to a client, said at that time if the client is then in the condition */
  private void plan(int at, int fd, String line, Predicate<Client> condition)
  {
    pending.add(new Pending(at, fd, line, condition));
    pending.sort((a, b) -> Integer.compare(a.at(), b.at()));
  }

  private void names(int fd, Client client, String channel)
  {
    List<String> nicks = new ArrayList<>(members.getOrDefault(channel, List.of()));
    clients.values().stream().filter(other -> other.channels.contains(channel)).forEach(other -> nicks.add(other.nick));
    if (!nicks.isEmpty())
      numeric(fd, client, "353", "= " + channel + " :" + String.join(" ", nicks));
    numeric(fd, client, "366", channel + " :End of /NAMES list.");
  }

  private void part(int fd, Client client, List<String> params)
  {
    if (params.isEmpty()) {
      numeric(fd, client, "461", "PART :Not enough parameters");
      return;
    }
    for (String channel : params.get(0).split(","))
      if (!members.containsKey(channel))
        numeric(fd, client, "403", channel + " :No such channel");
      else if (!client.channels.remove(channel))
        numeric(fd, client, "442", channel + " :You're not on that channel");
      else
        out(fd, ":" + client.prefix() + " PART " + channel + (params.size() > 1 ? " :" + params.get(1) : ""));
  }

  private void privmsg(int fd, Client client, List<String> params)
  {
    if (params.isEmpty()) {
      numeric(fd, client, "411", ":No recipient given (PRIVMSG)");
      return;
    }
    if (params.size() < 2 || params.get(1).isEmpty()) {
      numeric(fd, client, "412", ":No text to send");
      return;
    }
    if (params.size() > 2) { // a text with spaces but without its colon, of which a real server would only send the first word
      String text   = String.join(" ", params.subList(1, params.size()));
      String advice = Game.i18n.tr("Your message was not sent: a text holding spaces must start with a colon, as in PRIVMSG {0} :{1} "
                                       + "(a real server would only send {2}).",
                                   params.get(0), text, params.get(1));
      out(fd, ":" + NAME + " NOTICE " + client.nick + " :*** " + ascii(advice));
      return;
    }
    String target = params.get(0);
    if (target.startsWith("#")) {
      if (!members.containsKey(target))
        numeric(fd, client, "401", target + " :No such nick/channel");
      else if (!client.channels.contains(target))
        numeric(fd, client, "404", target + " :Cannot send to channel");
      else { // the message reaches the other members (but not its sender), and the one it is addressed to reacts
        Matcher addressed = ADDRESSED.matcher(params.get(1));
        if (addressed.matches())
          members.get(target)
              .stream()
              .filter(addressed.group(1)::equalsIgnoreCase)
              .findFirst()
              .ifPresent(nick -> ask(fd, client, nick, addressed.group(2), target));
      }
      return;
    }
    members.values()
        .stream()
        .flatMap(List::stream)
        .filter(target::equalsIgnoreCase)
        .findFirst()
        .ifPresentOrElse(nick -> ask(fd, client, nick, params.get(1), client.nick), () -> numeric(fd, client, "401", target + " :No such nick/channel"));
  }

  /**
   * Makes a simulated member react to a message of the client: sent to it in private (to being the nickname of the client), or
   * addressed to it on a channel (to being this channel, where the member addresses the client in turn)
   */
  private void ask(int fd, Client client, String nick, String message, String to)
  {
    client.told.add(nick);
    Answers answer = answers.get(nick);
    if (answer == null)
      return;
    String normalized = ascii(PosixWorld.text(message)).toLowerCase(Locale.ROOT); // the accents of the client's UTF-8 removed too
    boolean matches   = answer.words().stream().allMatch(forms -> forms.stream().anyMatch(normalized::contains));
    String reply    = (to.startsWith("#") ? client.nick + ": " : "") + (matches ? answer.answer() : answer.otherwise());
    plan(now() + 1, fd, ":" + member(nick) + " PRIVMSG " + to + " :" + reply, c -> true);
    if (matches) {
      client.answered.add(nick);
      return;
    }
    int failures   = client.failures.merge(nick, 1, Integer::sum);
    Object[] words = Stream.concat(Stream.of(client.nick), answer.words().stream().map(forms -> forms.get(0))).toArray();
    for (Hints hint : hints.getOrDefault(nick, List.of())) {
      String text = MessageFormat.format(hint.texts().get(Math.min(failures, hint.texts().size()) - 1), words);
      plan(now() + 3, fd, ":" + member(hint.nick()) + " PRIVMSG " + hint.channel() + " :" + text, // two seconds after the reply
           c -> c.channels.contains(hint.channel()) && !c.answered.contains(nick));
    }
  }

  @Override String describe()
  {
    StringBuilder sb = new StringBuilder(super.describe());
    for (Client client : clients.values())
      sb.append(", client ").append(client.nick == null ? "without nickname" : client.nick).append(client.registered ? "" : " (not registered)");
    return sb.toString();
  }
}
