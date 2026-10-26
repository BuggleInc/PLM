package plm.universe.posix;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * An IRC server (RFC 2812), exact to the byte as the usual servers: after registering with NICK and USER, the clients can JOIN
 * and PART channels, send PRIVMSG to a channel or to someone, ask the NAMES of a channel, PING the server and QUIT. The errors
 * are the usual numeric replies (421 unknown command, 451 not registered, 433 nickname in use, 461 not enough parameters...). As
 * most servers, it splits the lines on \n and ignores a \r before it, and the commands are case-insensitive.
 *
 * The other members of the channels are simulated: they say their lines at given times (only the clients in the channel at that
 * time receive them, as on a real server), and answer one exact private message after a second, or answer something else to any
 * other message. The texts are converted to ASCII, which the world only accepts for now (the translated texts lose their
 * accents). Everything the server sends can be cut in packets, so that the framing of the lines matters.
 */
public class IrcServer extends ProgramPeer {
  public static final String NAME     = "irc.buggleinc.com";
  private static final String VERSION = "plm-ircd-1.0";

  /** A line said on a channel by a simulated member, at a given time */
  private record Said(int at, String channel, String nick, String text) {}
  /** The answers of a simulated member to private messages: to the exact question, and to anything else */
  private record Answers(String question, String answer, String otherwise) {}
  /** A private message of a simulated member to a client, waiting for its time */
  private record Reply(int at, int fd, String nick, String text) {}

  /** A connected client, by descriptor */
  private static final class Client {
    String nick, user;
    boolean registered;
    final List<String> channels = new ArrayList<>();

    Client() {}

    Client(Client other)
    {
      nick       = other.nick;
      user       = other.user;
      registered = other.registered;
      channels.addAll(other.channels);
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
  private final List<Reply> replies               = new ArrayList<>();
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
    replies.addAll(other.replies);
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

  /** Makes a simulated member say a line on a channel at that time (in seconds) */
  public IrcServer say(int at, String channel, String nick, String text)
  {
    said.add(new Said(at, channel, nick, ascii(text)));
    said.sort((a, b) -> Integer.compare(a.at(), b.at()));
    return this;
  }

  /** Makes a simulated member answer the exact question in private one second later, and answer otherwise to anything else */
  public IrcServer answer(String nick, String question, String answer, String otherwise)
  {
    answers.put(nick, new Answers(question, ascii(answer), ascii(otherwise)));
    return this;
  }

  /** Cuts everything the server sends in packets of that size, one per second */
  public IrcServer packets(int size)
  {
    packetSize = size;
    return this;
  }

  /** The text in ASCII: the accents are removed, and the other characters become ? */
  private static String ascii(String text)
  {
    String res = Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    return res.replaceAll("[^\\x00-\\x7F]", "?");
  }

  // ---------------------------------------------------------------- the event loop

  @Override protected void start() { master = listen(port); }

  @Override protected boolean act()
  {
    int fd = accept(master);
    if (fd >= 0) {
      clients.put(fd, new Client());
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
    if (!said.isEmpty() && said.get(0).at() <= now()) {
      Said line = said.remove(0);
      clients.forEach((client, c) -> {
        if (c.channels.contains(line.channel()))
          out(client, ":" + member(line.nick()) + " PRIVMSG " + line.channel() + " :" + line.text());
      });
      return true;
    }
    for (Reply reply : replies)
      if (reply.at() <= now()) {
        replies.remove(reply);
        if (clients.containsKey(reply.fd()))
          out(reply.fd(), ":" + member(reply.nick()) + " PRIVMSG " + clients.get(reply.fd()).nick + " :" + reply.text());
        return true;
      }
    return false;
  }

  @Override protected int timer()
  {
    int res = said.isEmpty() ? -1 : said.get(0).at();
    for (Reply reply : replies)
      if (res < 0 || reply.at() < res)
        res = reply.at();
    return res;
  }

  @Override protected String waiting() { return clients.isEmpty() ? "waiting for a connection" : "waiting for a command"; }

  /** The prefix of a simulated member */
  private static String member(String nick) { return nick + "!" + nick.toLowerCase(Locale.ROOT) + "@buggleinc.com"; }

  /** Sends one or several lines (separated by \r\n) to a client, cut in packets if asked */
  private void out(int fd, String lines)
  {
    if (packetSize > 0)
      sendInPackets(fd, lines + "\r\n", packetSize, 1);
    else
      send(fd, lines + "\r\n");
  }

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
    }
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
    String target = params.get(0);
    if (target.startsWith("#")) {
      if (!members.containsKey(target))
        numeric(fd, client, "401", target + " :No such nick/channel");
      else if (!client.channels.contains(target))
        numeric(fd, client, "404", target + " :Cannot send to channel");
      return; // the message reaches the other members, but not its sender
    }
    boolean known = members.values().stream().anyMatch(nicks -> nicks.stream().anyMatch(target::equalsIgnoreCase));
    if (!known) {
      numeric(fd, client, "401", target + " :No such nick/channel");
      return;
    }
    Answers answer = answers.get(target);
    if (answer != null) {
      String nick = members.values().stream().flatMap(List::stream).filter(target::equalsIgnoreCase).findFirst().get();
      replies.add(new Reply(now() + 1, fd, nick, params.get(1).equals(answer.question()) ? answer.answer() : answer.otherwise()));
    }
  }

  @Override String describe()
  {
    StringBuilder sb = new StringBuilder(super.describe());
    clients.values().forEach(client -> sb.append(", client ").append(client.nick).append(client.registered ? "" : " (not registered)"));
    return sb.toString();
  }
}
