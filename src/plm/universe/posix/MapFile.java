package plm.universe.posix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * The .map file of an expected PosixWorld (see World.haveIO()): the system calls of the correction, with their arguments, so that
 * loading an exercise replays them on its initial world instead of running its correction. The kernel and the peers then rebuild
 * the trace, the timeline and the final state, which are deterministic. Each line holds the name of a method of PosixEntity and
 * its arguments separated by spaces, the texts and data being quoted as in Java (null for none), every character outside the
 * printable ASCII being escaped, so that the file is in ASCII.
 *
 * <pre>
 * PosixWorld "Greetings"
 * socket 2 1 0
 * connect 3 "10.0.0.1" 9001
 * recv 3 1024 0
 * write 1 "Hello\n"
 * processEnded 0
 * </pre>
 */
final class MapFile {
  private MapFile() {}

  /** The methods of PosixEntity that a file can call, by name */
  private static final Map<String, Method> methods = new HashMap<>();
  static
  {
    for (Method m : PosixEntityPrimitives.class.getMethods())
      methods.put(m.getName(), m);
    try {
      methods.put("processEnded", PosixEntity.class.getMethod("processEnded", Integer.class));
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
  }

  static void write(PosixWorld world, BufferedWriter out) throws IOException
  {
    out.write("PosixWorld " + quoted(world.getName()) + "\n");
    for (PosixWorld.Call call : world.calls) {
      out.write(call.name());
      for (Object argument : call.arguments())
        out.write(" " + (argument instanceof String text ? quoted(text) : String.valueOf(argument)));
      out.write("\n");
    }
  }

  /** The text between double quotes, in ASCII with its special characters escaped, or null */
  static String quoted(String text)
  {
    if (text == null)
      return "null";
    StringBuilder sb = new StringBuilder("\"");
    for (char c : text.toCharArray())
      switch (c) {
        case '"' -> sb.append("\\\"");
        case '\\' -> sb.append("\\\\");
        case '\n' -> sb.append("\\n");
        case '\r' -> sb.append("\\r");
        case '\t' -> sb.append("\\t");
        default -> sb.append(c >= ' ' && c < 0x7F ? String.valueOf(c) : String.format("\\u%04x", (int)c));
      }
    return sb.append('"').toString();
  }

  /**
   * The expected world written in the file: a copy of its initial world, on which the calls of the file are replayed. A malformed
   * file, or one whose replay fails, raises an IllegalArgumentException, which World.readFromFile() reports.
   */
  static PosixWorld replay(PosixWorld initial, BufferedReader in) throws IOException
  {
    PosixWorld res = new PosixWorld(initial);
    Line line      = new Line(in.readLine());
    if (!line.word().equals("PosixWorld") || !line.text().equals(res.getName()))
      throw new IllegalArgumentException("this file is not the one of world '" + res.getName() + "'");
    PosixEntity process = (PosixEntity)res.getEntity(0);
    for (String text = in.readLine(); text != null; text = in.readLine()) {
      line          = new Line(text);
      String name   = line.word();
      Method method = methods.get(name);
      if (method == null)
        throw new IllegalArgumentException("unknown call '" + name + "'");
      Class<?>[] types   = method.getParameterTypes();
      Object[] arguments = new Object[types.length];
      for (int i = 0; i < types.length; i++)
        if (types[i] == int.class)
          arguments[i] = line.number();
        else if (types[i] == Integer.class) {
          String word  = line.word();
          arguments[i] = word.equals("null") ? null : Integer.valueOf(word);
        } else {
          String data  = line.text();
          arguments[i] = types[i] == byte[].class && data != null ? data.getBytes(StandardCharsets.ISO_8859_1) : data;
        }
      line.end();
      try {
        method.invoke(process, arguments);
      } catch (InvocationTargetException | IllegalAccessException e) {
        Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
        throw new IllegalArgumentException("the replay of '" + text + "' fails: " + cause, cause);
      }
    }
    return res;
  }

  /** A line of the file, read word by word */
  private static final class Line {
    private final String content;
    private int pos;

    Line(String content)
    {
      if (content == null)
        throw new IllegalArgumentException("empty file");
      this.content = content;
    }

    /** The next word, up to a space or the end of the line */
    String word()
    {
      if (pos >= content.length())
        throw new IllegalArgumentException("missing word at the end of '" + content + "'");
      int end = content.indexOf(' ', pos);
      if (end < 0)
        end = content.length();
      String res = content.substring(pos, end);
      pos        = end + 1;
      return res;
    }

    int number() { return Integer.parseInt(word()); }

    boolean flag()
    {
      String word = word();
      if (!word.equals("true") && !word.equals("false"))
        throw new IllegalArgumentException("expected true or false instead of '" + word + "' in '" + content + "'");
      return word.equals("true");
    }

    /** The next text, quoted as quoted() does, or null */
    String text()
    {
      if (content.startsWith("null", pos) && (pos + 4 == content.length() || content.charAt(pos + 4) == ' ')) {
        pos += 5;
        return null;
      }
      if (pos >= content.length() || content.charAt(pos) != '"')
        throw new IllegalArgumentException("expected a quoted text at column " + pos + " of '" + content + "'");
      StringBuilder sb = new StringBuilder();
      for (pos++; pos < content.length() && content.charAt(pos) != '"'; pos++) {
        char c = content.charAt(pos);
        if (c == '\\' && pos + 1 < content.length()) {
          c = content.charAt(++pos);
          if (c == 'u' && pos + 5 <= content.length()) {
            sb.append((char)Integer.parseInt(content.substring(pos + 1, pos + 5), 16));
            pos += 4;
            continue;
          }
          c = switch (c) {
            case 'n' -> '\n';
            case 'r' -> '\r';
            case 't' -> '\t';
            case '"', '\\' -> c;
            default -> throw new IllegalArgumentException("unknown escape \\" + c + " in '" + content + "'");
          };
        }
        sb.append(c);
      }
      if (pos >= content.length())
        throw new IllegalArgumentException("unterminated text in '" + content + "'");
      pos += 2; // the quote and the space after it
      return sb.toString();
    }

    /** Checks that the line ends here */
    void end()
    {
      if (pos < content.length())
        throw new IllegalArgumentException("unexpected '" + content.substring(pos) + "' at the end of '" + content + "'");
    }
  }
}
