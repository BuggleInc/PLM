package plm.core;

import java.awt.Color;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class ValueSerializer {

  private static String getTypeRepresentation(Class<?> clazz)
  {
    // A component type that is itself an array (e.g. int[][]'s component is int[]) means we're one level above the deepest
    // level: no fixed tag needed here, each element (itself an array) carries its own tag when serialized recursively.
    if (clazz.isArray())
      return "";

    if (clazz == Integer.class || clazz == int.class)
      return "i";
    if (clazz == Double.class || clazz == double.class)
      return "f";
    if (clazz == Boolean.class || clazz == boolean.class)
      return "b";
    if (clazz == Character.class || clazz == char.class)
      return "c";
    if (clazz == Color.class)
      return "C";

    if (clazz == Object.class || clazz == String.class)
      return "";
    throw new IllegalArgumentException("Unknown serializable type: " + clazz.getSimpleName());
  }

  /* --- Serialization logic --- */
  public static String serialize(Object o)
  {
    if (o == null)
      return "Z";

    Class<?> clazz = o.getClass();
    if (o instanceof Collection || clazz.isArray()) {

      List<?> values;
      String typeRepresentation;

      if (o instanceof Collection<?> objects) {
        values             = Arrays.asList(objects.toArray());
        typeRepresentation = objects.isEmpty() ? "" : getTypeRepresentation(objects.iterator().next().getClass());
      } else {
        typeRepresentation = getTypeRepresentation(clazz.getComponentType());
        if (o instanceof int[] ints) {
          values = Arrays.stream(ints).boxed().toList();
        } else if (o instanceof double[] doubles) {
          values = Arrays.stream(doubles).boxed().toList();
        } else if (o instanceof boolean[] booleans) {
          // Arrays.stream is not implemented for booleans...
          List<Boolean> booleanValues = new ArrayList<>();
          for (boolean b : booleans) {
            Boolean boo = b ? Boolean.TRUE : Boolean.FALSE;
            booleanValues.add(boo);
          }
          values = booleanValues;
        } else if (o instanceof char[] chars) {
          List<Character> charValues = new ArrayList<>();
          for (char c : chars) {
            charValues.add(c);
          }
          values = charValues;
        } else {
          values = Arrays.stream((Object[])o).toList();
        }
      }

      return typeRepresentation + "[" + values.size() + values.stream().map(ValueSerializer::serialize).map(s -> ":" + s).collect(Collectors.joining()) + "]";
    }

    if (o instanceof String value) {
      String escaped = value.replace("\\", "\\\\").replace("\"", "\\\"");
      return "\"" + escaped + "\"";
    }

    String typeRepresentation = getTypeRepresentation(clazz);

    if (typeRepresentation.equals("b")) {
      return "b" + (Objects.equals(o, true) ? "1" : "0");
    }
    if (typeRepresentation.equals("C")) {
      // getRGB() packs alpha+red+green+blue into a single int; new Color(argb, true) below
      // reconstructs the exact same Color from it, alpha included.
      return "C" + ((Color)o).getRGB();
    }
    return typeRepresentation + o;
  }

  /* --- Deserialization logic --- */

  public static Object deserialize(String text)
  {
    Parser parser = new Parser(text);
    Object value  = parser.parseValue();
    if (!parser.isFinished()) {
      throw new IllegalArgumentException("Unexpected trailing characters.");
    }
    return value;
  }

  /**
   * Rebuilds a multi-dimensional int array (int[], int[][], int[][][], ...) from the generic Object[] tree that deserialize()
   * returns above its deepest array level.
   */
  public static Object toIntArray(Object o)
  {
    if (o instanceof int[])
      return o;

    Object[] array     = (Object[])o;
    Object[] converted = new Object[array.length];
    for (int i = 0; i < array.length; i++)
      converted[i] = toIntArray(array[i]);

    Class<?> componentType = converted.length > 0 ? converted[0].getClass() : int[].class;
    Object typedArray      = Array.newInstance(componentType, converted.length);
    for (int i = 0; i < converted.length; i++)
      Array.set(typedArray, i, converted[i]);
    return typedArray;
  }

  private static final class Parser {

    private final String text;
    private int pos = 0;

    Parser(String text) { this.text = text; }

    private static boolean isNumberChar(char c) { return Character.isDigit(c) || c == '-'; }

    private static boolean isDoubleChar(char c) { return Character.isDigit(c) || c == '-' || c == '.' || c == 'e' || c == 'E' || c == '+'; }

    /* ---------- Arrays ---------- */

    boolean isFinished() { return pos == text.length(); }

    Object parseValue()
    {
      char c = peek();

      if (c == 'Z') {
        pos++;
        return null;
      }

      if (c == '"') {
        return parseString();
      }

      if (c == 'i') {
        pos++;
        if (peek() == '[') {
          return parseIntArray();
        }
        return parseInt();
      }

      if (c == 'f') {
        pos++;
        if (peek() == '[') {
          return parseDoubleArray();
        }
        return parseDouble();
      }

      if (c == 'b') {
        pos++;
        if (peek() == '[') {
          return parseBooleanArray();
        }
        return parseBoolean();
      }

      if (c == 'c') {
        pos++;
        if (peek() == '[') {
          return parseCharArray();
        }
        return parseChar();
      }

      if (c == 'C') {
        pos++;
        if (peek() == '[') {
          return parseColorArray();
        }
        return parseColor();
      }

      if (c == '[') {
        return parseObjectArray();
      }

      throw new IllegalArgumentException("Unexpected character '" + c + "' at " + pos);
    }

    private int[] parseIntArray()
    {
      Object[] values = parseArrayContents();
      int[] result    = new int[values.length];
      for (int i = 0; i < values.length; i++) {
        result[i] = (Integer)values[i];
      }
      return result;
    }

    private double[] parseDoubleArray()
    {
      Object[] values = parseArrayContents();
      double[] result = new double[values.length];
      for (int i = 0; i < values.length; i++) {
        result[i] = (Double)values[i];
      }
      return result;
    }

    private boolean[] parseBooleanArray()
    {
      Object[] values  = parseArrayContents();
      boolean[] result = new boolean[values.length];
      for (int i = 0; i < values.length; i++) {
        result[i] = (Boolean)values[i];
      }
      return result;
    }

    private char[] parseCharArray()
    {
      Object[] values = parseArrayContents();
      char[] result   = new char[values.length];
      for (int i = 0; i < values.length; i++) {
        result[i] = (Character)values[i];
      }
      return result;
    }

    private Color[] parseColorArray()
    {
      Object[] values = parseArrayContents();
      Color[] result  = new Color[values.length];
      for (int i = 0; i < values.length; i++) {
        result[i] = (Color)values[i];
      }
      return result;
    }

    /* ---------- Primitive values ---------- */

    private Object[] parseObjectArray() { return parseArrayContents(); }

    private Object[] parseArrayContents()
    {
      expect('[');

      int size = parseUnsignedInt();

      Object[] values = new Object[size];

      for (int i = 0; i < size; i++) {
        expect(':');
        values[i] = parseValue();
      }

      expect(']');

      return values;
    }

    private Integer parseInt()
    {
      int start = pos;
      while (!isFinished() && isNumberChar(peek())) {
        pos++;
      }
      return Integer.parseInt(text.substring(start, pos));
    }

    private Double parseDouble()
    {
      int start = pos;
      while (!isFinished() && isDoubleChar(peek())) {
        pos++;
      }
      return Double.parseDouble(text.substring(start, pos));
    }

    /* ---------- Helpers ---------- */

    private Boolean parseBoolean()
    {
      char c = peek();
      pos++;
      return switch (c) {
        case '0' -> false;
        case '1' -> true;
        default -> throw new IllegalArgumentException("Invalid boolean");
      };
    }

    private Character parseChar()
    {
      char c = peek();
      pos++;
      return c;
    }

    private Color parseColor()
    {
      // true: interpret the int as including the alpha channel, matching getRGB()/serialize() above.
      Color c = new Color(parseInt(), true);

      // Try to return a canonical color such as Color.black so that == works as a comparator
      for (Color c2 :
           new Color[] {Color.white,     Color.WHITE,  Color.black, Color.BLACK, Color.blue,  Color.BLUE,      Color.cyan,       Color.CYAN,    Color.darkGray,
                        Color.DARK_GRAY, Color.gray,   Color.GRAY,  Color.green, Color.GREEN, Color.lightGray, Color.LIGHT_GRAY, Color.magenta, Color.MAGENTA,
                        Color.orange,    Color.ORANGE, Color.pink,  Color.PINK,  Color.red,   Color.RED,       Color.yellow,     Color.YELLOW})
        if (c2.equals(c))
          return c2;
      // Not found, return the newly created color
      return c;
    }

    private String parseString()
    {
      expect('"');

      StringBuilder sb = new StringBuilder();

      while (true) {
        if (isFinished()) {
          throw new IllegalArgumentException("Unterminated string");
        }

        char c = text.charAt(pos++);

        if (c == '\\') {
          if (isFinished()) {
            throw new IllegalArgumentException("Invalid escape");
          }
          sb.append(text.charAt(pos++));
        } else if (c == '"') {
          break;
        } else {
          sb.append(c);
        }
      }

      return sb.toString();
    }

    private int parseUnsignedInt()
    {
      int start = pos;
      while (!isFinished() && Character.isDigit(peek())) {
        pos++;
      }
      return Integer.parseInt(text.substring(start, pos));
    }

    private char peek() { return text.charAt(pos); }

    private void expect(char c)
    {
      if (isFinished() || text.charAt(pos) != c) {
        throw new IllegalArgumentException("Expected '" + c + "' at " + pos);
      }
      pos++;
    }
  }
}