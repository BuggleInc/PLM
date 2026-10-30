package plm.universe.posix;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import plm.core.model.Game;

/**
 * An authoritative DNS server over UDP (RFC 1035) for one zone, without recursion: it answers the queries for the names of its
 * zone with the records of type A, TXT and SRV given by the exercise (ANY giving all those of a name), NXDOMAIN for a missing name,
 * NOERROR without answer for an existing name without records of that type (including the names that only have descendants, as
 * RFC 8020), and REFUSED for the names outside its zone. It never compresses names. The TXT records of the queried name come in the
 * additional section of the other answers about this name, where the student finds the hints of the scenario without guessing
 * their type.
 *
 * Each query and each answer is noted decoded in the trace, so that the student sees how the server read the binary message. A
 * malformed query is answered FORMERR, with a note explaining the usual mistakes (byte order of the numbers, a name without its
 * length bytes or its final zero byte, bytes after the question...); a datagram too short to be a message, or holding an answer,
 * is ignored as real servers do, with a note.
 */
public class DnsServer extends ProgramPeer {
  public static final int A = 1, TXT = 16, SRV = 33, ANY = 255;
  private static final int NOERROR = 0, FORMERR = 1, NXDOMAIN = 3, NOTIMP = 4, REFUSED = 5;
  /** The TC bit of the flags, telling that the answer was truncated */
  private static final int TC  = 0x0200;
  private static final int TTL = 3600;
  /** The largest answer over UDP without EDNS, beyond which the answer is truncated (RFC 1035, 4.2.1) */
  private static final int UDP_LIMIT = 512;

  /** A record of the zone: its name (lowercase labels), its type, its data as sent, and its data as dig shows it */
  private record Record(List<String> name, int type, String data, String shown) {}

  /** A malformed query, with the explanation of the mistake */
  private static final class Malformed extends Exception {
    Malformed(String explanation) { super(explanation); }
  }

  private final int port;
  private final List<String> zone;
  private final List<Record> records = new ArrayList<>();
  private int fd;

  /** A server answering for the names of the zone (such as "buggleinc.lan") on that port of ip */
  public DnsServer(String ip, int port, String zone)
  {
    super(ip, "DNS server");
    this.port = port;
    this.zone = labels(zone);
  }

  private DnsServer(DnsServer other, Map<FileDescription, FileDescription> copies)
  {
    super(other, copies);
    port = other.port;
    zone = other.zone;
    fd   = other.fd;
    records.addAll(other.records); // never changed once the scenario is given
  }

  @Override protected DnsServer copy(Map<FileDescription, FileDescription> copies) { return new DnsServer(this, copies); }

  // ---------------------------------------------------------------- the zone, given by the exercise

  /** Adds an A record: the IPv4 address of the name */
  public DnsServer a(String name, String ip)
  {
    StringBuilder data = new StringBuilder();
    for (String part : ip.split("\\."))
      data.append((char)Integer.parseInt(part));
    return add(name, A, data.toString(), ip);
  }

  /** Adds a TXT record, whose text is sent in ASCII, in strings of at most 255 bytes */
  public DnsServer txt(String name, String text)
  {
    String ascii       = ascii(text);
    StringBuilder data = new StringBuilder();
    for (int i = 0; i < ascii.length(); i += 255) {
      String part = ascii.substring(i, Math.min(ascii.length(), i + 255));
      data.append((char)part.length()).append(part);
    }
    return add(name, TXT, data.toString(), PosixWorld.quoted(ascii));
  }

  /** Adds a SRV record (RFC 2782): the target host and port of a service, the lowest priority being tried first */
  public DnsServer srv(String name, int priority, int weight, int port, String target)
  {
    return add(name, SRV, u16(priority) + u16(weight) + u16(port) + wire(labels(target)), priority + " " + weight + " " + port + " " + shown(labels(target)));
  }

  private DnsServer add(String name, int type, String data, String shown)
  {
    records.add(new Record(labels(name), type, data, shown));
    return this;
  }

  // ---------------------------------------------------------------- the program

  @Override protected void start() { fd = bindDatagrams(port); }

  @Override protected boolean act()
  {
    Datagram query = receiveDatagram(fd);
    if (query == null)
      return false;
    String answer = answer(query.data());
    if (answer != null)
      sendDatagram(fd, answer, query.ip(), query.port());
    return true;
  }

  @Override protected String waiting() { return "waiting for a query"; }

  /** The answer to a query, or null if it must be ignored, noting in the trace what the server understood */
  private String answer(String query)
  {
    if (query.length() < 12) {
      note(Game.i18n.tr("Ignored: a DNS message starts with a header of 12 bytes, but this datagram only has {0} bytes.", String.valueOf(query.length())));
      return null;
    }
    int id = u16(query, 0), flags = u16(query, 2), questions = u16(query, 4);
    if ((flags & 0x8000) != 0) {
      note(Game.i18n.tr("Ignored: the QR bit (the highest bit of the flags) is set, which marks an answer, not a query."));
      return null;
    }
    int end = 12;
    try {
      if ((flags >> 11 & 0xF) != 0) {
        note(Game.i18n.tr("NOTIMP: the opcode (bits 11 to 14 of the flags) must be 0, for a standard query."));
        return header(id, flags, NOTIMP, 0, 0, 0);
      }
      if (questions != 1)
        throw new Malformed(questions == 0x0100
                                ? Game.i18n.tr("The header announces 256 questions (01 00), where a query has exactly one (00 01): "
                                               + "the numbers of DNS are written in network byte order, the most significant byte first.")
                                : Game.i18n.tr("The header announces {0} questions, where a query has exactly one.", String.valueOf(questions)));
      if (u16(query, 6) != 0 || u16(query, 8) != 0)
        throw new Malformed(Game.i18n.tr("The header announces answers or authority records, which a query does not have: their counts must be 0."));
      List<String> name = new ArrayList<>();
      end               = readName(query, 12, name);
      if (end + 4 > query.length())
        throw new Malformed(Game.i18n.tr("The question ends after its name: its type and its class (2 bytes each) are missing."));
      int type = u16(query, end), qclass = u16(query, end + 2);
      end += 4;
      if (end < query.length() && u16(query, 10) == 0) // the additional records (such as the EDNS ones) are ignored
        throw new Malformed(
            Game.i18n.tr("{0} bytes follow the question, which ends the query: only send the bytes of the message.", String.valueOf(query.length() - end)));
      note("query " + typeName(type) + " " + shown(name) + " (id " + id + ")");
      if (name.stream().anyMatch(label -> label.contains(".")))
        note(Game.i18n.tr("A label of this name holds a dot: each part of the name between the dots is a label of its own, preceded by its length."));
      String question = query.substring(12, end);
      if (qclass != 1) {
        note(qclass == 0x0100 ? Game.i18n.tr("REFUSED: the class 256 (01 00) is not IN (00 01), the class of Internet: "
                                             + "the numbers are written most significant byte first.")
                              : Game.i18n.tr("REFUSED: this server only knows the class IN (1) of Internet, not the class {0}.", String.valueOf(qclass)));
        return header(id, flags, REFUSED, 1, 0, 0) + question;
      }
      List<String> key = name.stream().map(label -> label.toLowerCase(Locale.ROOT)).toList();
      if (!under(key, zone)) {
        note(Game.i18n.tr("REFUSED: this server only answers for the names of {0}, and does not ask the other servers (no recursion).", shown(zone)));
        return header(id, flags, REFUSED, 1, 0, 0) + question;
      }
      if (!key.equals(zone) && records.stream().noneMatch(r -> under(r.name(), key))) {
        note("answer NXDOMAIN: " + Game.i18n.tr("no such name in {0}", shown(zone)));
        return header(id, flags, NXDOMAIN, 1, 0, 0) + question;
      }
      List<Record> answers = records.stream().filter(r -> r.name().equals(key) && (type == ANY || r.type() == type)).toList();
      List<Record> hints   = type == ANY || type == TXT ? List.of() : records.stream().filter(r -> r.name().equals(key) && r.type() == TXT).toList();
      int swapped          = (type & 0xFF) << 8 | type >> 8;
      if (answers.isEmpty() && swapped != type && List.of(A, TXT, SRV, ANY).contains(swapped))
        note(Game.i18n.tr("The type {0} looks like the type {1} written least significant byte first.", String.valueOf(type), String.valueOf(swapped)));
      StringBuilder res = new StringBuilder(header(id, flags, NOERROR, 1, answers.size(), hints.size())).append(question);
      for (Record r : answers)
        res.append(encode(r));
      for (Record r : hints)
        res.append(encode(r));
      note("answer NOERROR: " + (answers.isEmpty() ? Game.i18n.tr("no record of this type") : describe(answers)) +
           (hints.isEmpty() ? "" : "; additional: " + describe(hints)));
      if (res.length() > UDP_LIMIT) {
        note(Game.i18n.tr("The answer is longer than the {0} bytes allowed over UDP: it is truncated (TC bit).", String.valueOf(UDP_LIMIT)));
        return header(id, flags, TC | NOERROR, 1, 0, 0) + question;
      }
      return res.toString();
    } catch (Malformed e) {
      note("FORMERR: " + e.getMessage());
      return header(id, flags, FORMERR, 0, 0, 0);
    }
  }

  /** Reads the name starting at offset into labels, and returns the offset after it */
  private static int readName(String query, int offset, List<String> labels) throws Malformed
  {
    int length = 0;
    while (true) {
      if (offset >= query.length())
        throw new Malformed(Game.i18n.tr("The name runs past the end of the message: it ends with a zero byte.") + textual(query));
      int n = query.charAt(offset);
      if (n == 0)
        break;
      if (n >= 0xC0)
        throw new Malformed(
            Game.i18n.tr("The byte {0} starts a compression pointer (its two highest bits are set), "
                             + "but there is no earlier name to point to in a question. Each label of the name must be preceded by its length, at most 63.",
                         String.valueOf(n)));
      if (n > 63)
        throw new Malformed(Game.i18n.tr("The byte {0} announces a label of {0} bytes, but a label has at most 63 bytes.", String.valueOf(n)) + textual(query));
      if (offset + 1 + n > query.length())
        throw new Malformed(Game.i18n.tr("The byte {0} announces a label of {0} bytes, but the message ends before.", String.valueOf(n)) + textual(query));
      labels.add(query.substring(offset + 1, offset + 1 + n));
      offset += 1 + n;
      length += 1 + n;
    }
    if (length + 1 > 255)
      throw new Malformed(Game.i18n.tr("The name is {0} bytes long, while a name has at most 255 bytes.", String.valueOf(length + 1)));
    return offset + 1;
  }

  /** A hint ending the explanation of a malformed name, if the name looks written as text */
  private static String textual(String query)
  {
    if (query.length() > 12 && query.charAt(12) >= '0' && query.charAt(12) <= '9')
      return " " + Game.i18n.tr("The length of a label is one byte, not its digits written as text.");
    if (query.indexOf('.', 12) >= 0)
      return " " + Game.i18n.tr("The dots of the name are not written: each label is preceded by a byte giving its length.");
    return "";
  }

  /** The header of an answer to the query of that id and flags, with that response code (and possibly TC) and these counts */
  private static String header(int id, int flags, int rcode, int questions, int answers, int additional)
  {
    int authoritative = (rcode & 0xF) == NOERROR || rcode == NXDOMAIN ? 0x0400 : 0; // AA, for the answers from the zone
    int res           = 0x8000 | (flags & 0x7900) | authoritative | rcode;          // QR set, opcode and RD kept, RA unset
    return u16(id) + u16(res) + u16(questions) + u16(answers) + u16(0) + u16(additional);
  }

  /** A record as sent: its name, type, class IN, time to live, and the length of its data before the data */
  private static String encode(Record r)
  {
    return wire(r.name()) + u16(r.type()) + u16(1) + u16(TTL >> 16) + u16(TTL & 0xFFFF) + u16(r.data().length()) + r.data();
  }

  /** The records as dig shows them, without their name */
  private static String describe(List<Record> records)
  {
    return records.stream().map(r -> typeName(r.type()) + " " + r.shown()).collect(Collectors.joining(", "));
  }

  /** The name in the wire format: each label preceded by its length, then a zero byte */
  private static String wire(List<String> labels)
  {
    StringBuilder res = new StringBuilder();
    for (String label : labels)
      res.append((char)label.length()).append(label);
    return res.append('\0').toString();
  }

  private static List<String> labels(String name) { return List.of(name.toLowerCase(Locale.ROOT).split("\\.")); }

  /** Whether the name is the other one or under it */
  private static boolean under(List<String> name, List<String> other)
  {
    return name.size() >= other.size() && name.subList(name.size() - other.size(), name.size()).equals(other);
  }

  /** The name as dig shows it: the dots inside the labels escaped, and the other bytes that are not printable in decimal */
  private static String shown(List<String> labels)
  {
    StringBuilder res = new StringBuilder();
    for (String label : labels) {
      for (char c : label.toCharArray())
        res.append(c == '.' || c == '\\' ? "\\" + c : c > ' ' && c < 0x7F ? String.valueOf(c) : String.format("\\%03d", (int)c));
      res.append('.');
    }
    return res.isEmpty() ? "." : res.toString();
  }

  private static String typeName(int type)
  {
    return switch (type) {
      case A -> "A";
      case TXT -> "TXT";
      case SRV -> "SRV";
      case ANY -> "ANY";
      default -> "TYPE" + type;
    };
  }

  private static int u16(String data, int offset) { return data.charAt(offset) << 8 | data.charAt(offset + 1); }

  private static String u16(int value) { return "" + (char)(value >> 8 & 0xFF) + (char)(value & 0xFF); }
}
