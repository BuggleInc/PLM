package plm.core;

import java.util.Vector;

public class ValueSerializer {
  /* --- Serialization logic --- */
  public static String serialize(Object o)
  {
    StringBuilder sb = new StringBuilder();
    if (o == null) {
      return "Z";
    }

    if (o.getClass().equals(Vector.class) || o.getClass().isArray()) {
      sb.append('[');
      // Primitive types must be handled explicitely, as int cannot be casted to Object for a generic case.
      if (o.getClass().getComponentType().equals(Integer.TYPE)) {
        int[] a = (int[])o;
        sb.append(a.length);
        for (int i : a)
          sb.append(":i" + i);
      } else if (o.getClass().getComponentType().equals(Double.TYPE)) {
        double[] a = (double[])o;
        sb.append(a.length);
        for (double i : a)
          sb.append(":f" + i);
      } else if (o.getClass().getComponentType().equals(Boolean.TYPE)) {
        boolean[] a = (boolean[])o;
        sb.append(a.length);
        for (boolean b : a)
          sb.append(b ? ":b1" : ":b0");

      } else if (o instanceof Object[]) {
        Object[] a = (Object[])o;
        sb.append(a.length);
        for (Object o2 : a) {
          if (o2 == null)
            sb.append(":Z");
          else if (o2 instanceof Integer)
            sb.append(":i" + o2);
          else if (o2 instanceof Double)
            sb.append(":f" + o2);
          else if (o2 instanceof Boolean)
            sb.append(((Boolean)o2) ? ":b1" : ":b0");
          else if (o2 instanceof String) {
            String escaped = ((String)o2).replace("\\", "\\\\").replace("\"", "\\\"");
            sb.append(":\"" + escaped + "\"");
          } else if (o2.getClass().equals(Vector.class) || o2.getClass().isArray())
            sb.append(":" + serialize(o2));
          else
            throw new UnsupportedOperationException("ValueFormatter.serialize: unsupported array component type: " + o2.getClass().getName());
        }
      }
      sb.append(']');
    } else if (o instanceof String) {
      String escaped = ((String)o).replace("\\", "\\\\").replace("\"", "\\\"");
      sb.append("\"" + escaped + "\"");
    } else if (o instanceof Boolean) {
      Boolean b = (Boolean)o;
      sb.append(b ? "b1" : "b0");
    } else if (o instanceof Integer) {
      sb.append("i" + o);
    } else if (o instanceof Double) {
      sb.append("f" + o);
    } else {
      sb.append(o.toString());
    }
    return sb.toString();
  }

  /* --- Deserialization logic --- */
  public static Object deserialize(String input)
  {
    if (input == null || input.equals("Z"))
      return null;
    return new Parser(input).parse();
  }

  private static class Parser {
    private final String input;
    private int pos = 0;

    Parser(String input) { this.input = input; }

    Object parse()
    {
      if (pos >= input.length()) {
        throw new IllegalArgumentException("Unexpected end of input at position " + pos);
      }

      char c = input.charAt(pos);
      if (c == 'Z')
        return null;
      if (c == '[')
        return parseArray();
      if (c == '"')
        return parseString();
      return parsePrimitive();
    }

    private Object[] parseArray()
    {
      pos++; // Skip '['
      if (input.charAt(pos) == '0') {
        pos += 2;
        return new Object[0];
      }

      int colonIdx = input.indexOf(':', pos);
      if (colonIdx == -1) {
        throw new IllegalArgumentException("Expected ':' after array length at position " + pos);
      }

      int len = Integer.parseInt(input.substring(pos, colonIdx));
      pos     = colonIdx + 1; // Skip ':'

      Object[] arr = new Object[len];
      for (int i = 0; i < len; i++) {
        arr[i] = parse();
        if (i < len - 1) {
          if (input.charAt(pos) != ':') {
            throw new IllegalArgumentException("Expected ':' between array elements at position " + pos);
          }
          pos++; // Skip ':'
        }
      }

      if (pos >= input.length() || input.charAt(pos) != ']') {
        throw new IllegalArgumentException("Expected ']' at end of array at position " + pos);
      }
      pos++; // Skip ']'
      return arr;
    }

    private String parseString()
    {
      pos++; // Skip opening '"'
      StringBuilder sb = new StringBuilder();
      while (pos < input.length()) {
        char c = input.charAt(pos);
        if (c == '\\' && pos + 1 < input.length() && input.charAt(pos + 1) == '"') {
          sb.append('"');
          pos += 2;
        } else if (c == '"') {
          pos++; // Skip closing '"'
          return sb.toString();
        } else {
          sb.append(c);
          pos++;
        }
      }
      throw new IllegalArgumentException("Unterminated string starting at position " + (pos - sb.length() - 1));
    }

    private Object parsePrimitive()
    {
      int start = pos;
      while (pos < input.length() && input.charAt(pos) != ':' && input.charAt(pos) != ']') {
        pos++;
      }
      char typeHint = input.charAt(start);
      String val    = input.substring(start + 1, pos);

      try {
        switch (typeHint) {
          case 'i':
            return Integer.parseInt(val);
          case 'b':
            return val.equals("1");
          case 'f':
            return Double.parseDouble(val);
          default: // Fallback if format string is exhausted or mismatched
            throw new IllegalArgumentException("Parse error: Invalid type hint '" + typeHint + "' at position " + start + " in input \"" + input + "\"");
        }
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException(e);
      }
    }
  }
}