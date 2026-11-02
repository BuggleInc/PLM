package plm.universe.posix;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import plm.core.model.Game;

/**
 * The XML of an XMPP stream (RFC 6120, 11), read as it arrives: the header of the stream, then its top-level elements (the
 * stanzas), each read once it arrived whole, and the end of the stream. The XML is the restricted one of XMPP: no comments, no
 * processing instructions (but the XML declaration starting a stream), no DTD, and only the predefined entities and the
 * character references. The names keep their prefix (such as stream:features), and the xmlns are attributes as the others.
 */
final class Xml {
  private Xml() {}

  /** An element, with its attributes in their order, and its children: elements, and texts with their references decoded */
  record Element(String name, Map<String, String> attributes, List<Object> children) {
    String attribute(String key) { return attributes.get(key); }

    /** The first child element with that name, or null */
    Element child(String name)
    {
      for (Object child : children)
        if (child instanceof Element element && element.name().equals(name))
          return element;
      return null;
    }

    /** The texts directly in this element, joined */
    String text()
    {
      StringBuilder res = new StringBuilder();
      for (Object child : children)
        if (child instanceof String text)
          res.append(text);
      return res.toString();
    }
  }

  /** What comes next in a stream, and its length in the data: the header of a stream, a top-level element, or the end of the stream */
  record Unit(int length, Element header, Element element, boolean end) {}

  /** A mistake ending the stream, with the condition of the stream error (urn:ietf:params:xml:ns:xmpp-streams) and its explanation */
  static final class StreamError extends Exception {
    final String condition;

    StreamError(String condition, String explanation)
    {
      super(explanation);
      this.condition = condition;
    }
  }

  /** The data ends before the XML read */
  private static final class Incomplete extends Exception {
    Incomplete() { super(null, null, false, false); }
  }

  /**
   * What comes next in the data of a stream, or null if it did not arrive whole yet. The header (with its optional XML
   * declaration) is expected first in a stream, and may come again to restart it if restarting.
   */
  static Unit next(String data, boolean opening, boolean restarting) throws StreamError
  {
    Reader reader = new Reader(data);
    try {
      return reader.unit(opening, restarting);
    } catch (Incomplete e) {
      return null;
    }
  }

  /** The text escaped for XML, as the content of an element, or as the value of an attribute between that quote if positive */
  static String escape(String text, char quote)
  {
    StringBuilder res = new StringBuilder();
    for (char c : text.toCharArray())
      switch (c) {
        case '&' -> res.append("&amp;");
        case '<' -> res.append("&lt;");
        case '>' -> res.append(quote == 0 ? "&gt;" : ">");
        case '\'' -> res.append(quote == '\'' ? "&apos;" : "'");
        case '"' -> res.append(quote == '"' ? "&quot;" : "\"");
        default -> res.append(c);
      }
    return res.toString();
  }

  private static boolean isSpace(char c) { return c == ' ' || c == '\t' || c == '\r' || c == '\n'; }

  private static StreamError notWellFormed(String explanation) { return new StreamError("not-well-formed", explanation); }

  private static StreamError restricted()
  {
    return new StreamError("restricted-xml",
                           Game.i18n.tr("XMPP forbids the comments (<!-- -->), the processing instructions (<? ?>) and the DTD (<!DOCTYPE>) in a stream."));
  }

  /** Reads the XML of the data from its start, throwing Incomplete where the data ends too early */
  private static final class Reader {
    private final String data;
    private int pos;

    Reader(String data) { this.data = data; }

    Unit unit(boolean opening, boolean restarting) throws StreamError, Incomplete
    {
      skipSpaces();
      if (pos == data.length())
        throw new Incomplete(); // only spaces so far
      if (opening || restarting && (startsWith("<?xml") || startsWith("<stream:stream"))) {
        if (startsWith("<?xml")) {
          int end = data.indexOf("?>", pos);
          if (end < 0)
            throw new Incomplete();
          pos = end + 2;
          skipSpaces();
        } else if (startsWith("<?") || startsWith("<!"))
          throw restricted();
        if (!startsWith("<stream:stream"))
          throw new StreamError("bad-format",
                                Game.i18n.tr("A stream starts with its header, the element <stream:stream> (after the optional XML declaration)."));
        Element header = startTag();
        if (header.children() == null)
          throw notWellFormed(Game.i18n.tr("The header <stream:stream/> ends the stream at once: it must stay open, as <stream:stream ...>."));
        return new Unit(pos, new Element(header.name(), header.attributes(), List.of()), null, false);
      }
      if (startsWith("</")) {
        pos += 2;
        String name = name();
        skipSpaces();
        expect('>');
        if (!name.equals("stream:stream"))
          throw notWellFormed(Game.i18n.tr("</{0}> closes no element: the stanzas are the elements in the stream, and </stream:stream> ends it.", name));
        return new Unit(pos, null, null, true);
      }
      if (startsWith("<?") || startsWith("<!"))
        throw restricted();
      if (data.charAt(pos) != '<')
        throw notWellFormed(Game.i18n.tr("Some text is outside of any stanza: only the elements and some spaces may come between the stanzas."));
      Element element = element();
      return new Unit(pos, null, element, false);
    }

    /** Whether the data continues with that text (Incomplete if the data ends before telling) */
    private boolean startsWith(String text) throws Incomplete
    {
      for (int i = 0; i < text.length(); i++) {
        if (pos + i == data.length())
          throw new Incomplete();
        if (data.charAt(pos + i) != text.charAt(i))
          return false;
      }
      return true;
    }

    private char peek() throws Incomplete
    {
      if (pos == data.length())
        throw new Incomplete();
      return data.charAt(pos);
    }

    private void skipSpaces()
    {
      while (pos < data.length() && isSpace(data.charAt(pos)))
        pos++;
    }

    private void expect(char c) throws StreamError, Incomplete
    {
      if (peek() != c)
        throw notWellFormed(Game.i18n.tr("The character {0} was expected here, but {1} came.", String.valueOf(c), String.valueOf(peek())));
      pos++;
    }

    /** The name of an element or of an attribute */
    private String name() throws StreamError, Incomplete
    {
      int start = pos;
      while (!isSpace(peek()) && "/>=<&\"'".indexOf(peek()) < 0)
        pos++;
      if (pos == start || Character.isDigit(data.charAt(start)) || data.charAt(start) == '-' || data.charAt(start) == '.')
        throw notWellFormed(Game.i18n.tr("A < starts a tag, followed by the name of an element: a < in a text is written &lt;."));
      return data.substring(start, pos);
    }

    /** A start tag: an element with its attributes, whose children are null if it is empty (as <x/>), or still to read */
    private Element startTag() throws StreamError, Incomplete
    {
      expect('<');
      String name                    = name();
      Map<String, String> attributes = new LinkedHashMap<>();
      while (true) {
        boolean spaced = isSpace(peek());
        skipSpaces();
        if (startsWith("/>")) {
          pos += 2;
          return new Element(name, attributes, null);
        }
        if (peek() == '>') {
          pos++;
          return new Element(name, attributes, new ArrayList<>());
        }
        if (!spaced)
          throw notWellFormed(Game.i18n.tr("The attributes of <{0}> must be separated by spaces.", name));
        String attribute = name();
        skipSpaces();
        if (peek() != '=')
          throw notWellFormed(Game.i18n.tr("The attribute {0} of <{1}> must have a value, as {0}=''...''.", attribute, name));
        pos++;
        skipSpaces();
        char quote = peek();
        if (quote != '\'' && quote != '"')
          throw notWellFormed(Game.i18n.tr("The value of the attribute {0} of <{1}> must be quoted, with '' or \".", attribute, name));
        pos++;
        if (attributes.put(attribute, text(quote)) != null)
          throw notWellFormed(Game.i18n.tr("The attribute {0} is given twice in <{1}>.", attribute, name));
        pos++; // the closing quote
      }
    }

    /** An element, whole */
    private Element element() throws StreamError, Incomplete
    {
      Element element = startTag();
      if (element.children() == null)
        return new Element(element.name(), element.attributes(), List.of());
      while (true) {
        if (startsWith("</")) {
          pos += 2;
          String name = name();
          skipSpaces();
          expect('>');
          if (!name.equals(element.name()))
            throw notWellFormed(
                Game.i18n.tr("The element <{0}> is closed by </{1}>: each element is closed by its own name, the last opened first.", element.name(), name));
          return element;
        }
        if (startsWith("<![CDATA[")) {
          int end = data.indexOf("]]>", pos);
          if (end < 0)
            throw new Incomplete();
          element.children().add(data.substring(pos + 9, end));
          pos = end + 3;
        } else if (startsWith("<?") || startsWith("<!"))
          throw restricted();
        else if (peek() == '<')
          element.children().add(element());
        else
          element.children().add(text('<'));
      }
    }

    /** A text up to that character (excluded), with its references decoded */
    private String text(char end) throws StreamError, Incomplete
    {
      StringBuilder res = new StringBuilder();
      while (peek() != end) {
        char c = peek();
        if (c == '<')
          throw notWellFormed(Game.i18n.tr("The value of an attribute cannot hold a <: it is written &lt;."));
        if (c == '&')
          res.append(reference());
        else {
          res.append(c);
          pos++;
        }
      }
      return res.toString();
    }

    /** An entity or character reference, such as &lt; or &#60;, decoded */
    private String reference() throws StreamError, Incomplete
    {
      int end = pos + 1;
      while (end < data.length() && end - pos < 10 && data.charAt(end) != ';' && !isSpace(data.charAt(end)) && data.charAt(end) != '&' &&
             data.charAt(end) != '<')
        end++;
      if (end == data.length())
        throw new Incomplete();
      String name = data.substring(pos + 1, end);
      String res  = switch (name) {
        case "lt" -> "<";
        case "gt" -> ">";
        case "amp" -> "&";
        case "apos" -> "'";
        case "quot" -> "\"";
        default -> {
          try {
            if (name.matches("#[0-9]+"))
              yield new String(Character.toChars(Integer.parseInt(name.substring(1))));
            if (name.matches("#x[0-9a-fA-F]+"))
              yield new String(Character.toChars(Integer.parseInt(name.substring(2), 16)));
          } catch (IllegalArgumentException e) { // a number too large for a character
          }
          yield null;
        }
      };
      if (data.charAt(end) != ';' || res == null)
        throw notWellFormed(Game.i18n.tr("A & starts a reference, such as &lt; for < or &amp; for &: a & in a text is written &amp;."));
      pos = end + 1;
      return res;
    }
  }
}
