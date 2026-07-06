package plm.universe.bat;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import org.python.core.PyInstance;
import plm.core.lang.ProgrammingLanguage;

class ValueFormatter {
  // Collapses List, RecList, Integer[], Vector into one canonical internal type
  static Object normalize(Object value)
  {
    if (value == null)
      return null;
    if (value instanceof List) {
      List<?> l = (List<?>)value;
      int[] res = new int[l.size()];
      for (int i = 0; i < l.size(); i++)
        res[i] = (Integer)l.get(i);

      return res;
    }
    if (value instanceof lessons.recursion.cons.universe.RecList) {
      value = ((lessons.recursion.cons.universe.RecList)value).toArray();
    }
    if (value instanceof scala.collection.immutable.List) {
      scala.collection.immutable.List<?> sl = (scala.collection.immutable.List<?>)value;
      int[] res                             = new int[sl.size()];
      scala.collection.Iterator<?> it       = sl.iterator();
      int i                                 = 0;
      while (it.hasNext())
        res[i++] = (Integer)it.next();
      return res;
    }
    if (value.getClass().isArray() && value.getClass().getComponentType().equals(Integer.class)) {
      Integer[] orig = (Integer[])value;
      if (orig.length == 0 || (orig.length == 1 && orig[0] == null))
        return new int[] {};
      int[] res = new int[orig.length];
      for (int i = 0; i < res.length; i++)
        res[i] = orig[i];
      return res;
    }
    return value;
  }

  static boolean equals(Object o1, Object o2)
  {
    o1 = ValueFormatter.normalize(o1);
    o2 = ValueFormatter.normalize(o2);

    if (o1 == null && o2 == null)
      return true;
    if (o1 == null)
      return isEmptyArray(o2);
    if (o2 == null)
      return isEmptyArray(o1);

    if (o1.getClass().isArray() && o2.getClass().isArray()) {
      if (!o1.getClass().getComponentType().equals(o2.getClass().getComponentType()))
        return false; // not same type in both arrays

      if (o1.getClass().getComponentType().equals(Integer.TYPE)) {
        return Arrays.equals((int[])o1, (int[])o2);
      } else if (o1.getClass().getComponentType().equals(String.class)) {
        return Arrays.equals((String[])o1, (String[])o2);
      } else if (o1.getClass().getComponentType().equals(Object.class)) {
        return Arrays.equals((Object[])o1, (Object[])o2);
      } else
        throw new UnsupportedOperationException("ValueFormatter.equals: unsupported array component type: " +
                                                o1.getClass().getComponentType().getName());
    }
    if (o1.getClass().isArray() || o2.getClass().isArray())
      return false; // The other cannot be an array because of previous test
    return o1.equals(o2);
  }
  private static boolean isEmptyArray(Object o)
  {
    return o.getClass().isArray() && java.lang.reflect.Array.getLength(o) == 0;
  }

  /* --- Formatting logic --- */
  static private void openArray(StringBuilder sb, ProgrammingLanguage pl)
  {
    if (pl.isJava()) {
      sb.append("{");
    } else if (pl.isScala()) {
      sb.append("Array(");
    } else if (pl.isPython()) {
      sb.append("[");
    } else {
      throw new RuntimeException("Please port me to " + pl.getLang());
    }
  }
  static private void closeArray(StringBuilder sb, ProgrammingLanguage pl)
  {
    if (pl.isJava())
      sb.append("}");
    else if (pl.isScala())
      sb.append(")");
    else if (pl.isPython())
      sb.append("]");
    else
      throw new RuntimeException("Please port me to " + pl.getLang());
  }

  static String format(Object o, ProgrammingLanguage pl)
  {
    StringBuilder sb = new StringBuilder();
    if (o == null) {
      if (pl.isScala())
        return "Nil";
      else if (pl.isPython())
        return "None";
      else if (pl.isJava())
        return "null";
      else
        throw new RuntimeException("Please port me to " + pl.getLang());
    }

    if (o instanceof String[]) {
      openArray(sb, pl);

      String[] a = (String[])o;
      for (String i : a)
        sb.append(i + ",");
      if (a.length > 0) // Don't kill the last comma if there is none
        sb.deleteCharAt(sb.length() - 1);

      closeArray(sb, pl);

    } else if (o.getClass().equals(Vector.class) || o.getClass().isArray()) {
      if (o.getClass().equals(Vector.class))
        o = ValueFormatter.normalize(o);

      openArray(sb, pl);
      if (o.getClass().getComponentType().equals(Integer.TYPE)) {
        int[] a = (int[])o;
        for (int i : a)
          sb.append(i + ",");

        if (a.length > 0) // Don't kill the last comma if there is none
          sb.deleteCharAt(sb.length() - 1);
      } else if (o.getClass().getComponentType().equals(Integer.class)) {
        Integer[] a = (Integer[])o;
        for (Integer i : a)
          sb.append(i + ",");

        if (a.length > 0) // Don't kill the last comma if there is none
          sb.deleteCharAt(sb.length() - 1);

      } else {
        throw new RuntimeException("Unhandled internal type (only Array<int> and Array<Integer> are handled so far)");
      }
      closeArray(sb, pl);
    } else if (o instanceof Boolean) {
      Boolean b = (Boolean)o;
      if (pl.isJava() || pl.isScala()) {
        sb.append(b ? "true" : "false");
      } else if (pl.isPython()) {
        sb.append(b ? "True" : "False");
      } else {
        throw new RuntimeException("Please port me to " + pl.getLang());
      }
    } else if (o instanceof String) {
      sb.append("\"" + o + "\"");
    } else if (o instanceof PyInstance) {
      sb.append(((PyInstance)o).__str__());
    } else {
      sb.append(o.toString());
    }
    return sb.toString();
  }
  /* --- Serialization logic --- */
  static String serialize(Object o)
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
      if (input.charAt(pos) == '0')
        return new Object[0];

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