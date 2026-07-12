package plm.core;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ValueSerializerTest {

  // ---------------------------------------------------------------
  // serialize()
  // ---------------------------------------------------------------

  @Test void serialize_null_returnsNullString() { assertEquals("Z", ValueSerializer.serialize(null)); }

  @Test void serialize_boolean_returnsOneOrZero()
  {
    assertEquals("b1", ValueSerializer.serialize(true));
    assertEquals("b0", ValueSerializer.serialize(false));
  }

  @Test void serialize_string_isQuotedAndEscaped()
  {
    assertEquals("\"hello\"", ValueSerializer.serialize("hello"));
    assertEquals("\"he\\\"llo\"", ValueSerializer.serialize("he\"llo"));
  }

  @Test void serialize_intArray_returnsFormattedString() { assertEquals("i[3:i1:i2:i3]", ValueSerializer.serialize(new int[] {1, 2, 3})); }
  @Test void serialize_IntegerArray_returnsFormattedString() { assertEquals("i[2:i1:i2]", ValueSerializer.serialize(new Integer[] {1, 2})); }

  @Test void serialize_doubleArray_returnsFormattedString() { assertEquals("f[2:f1.5:f-2.2]", ValueSerializer.serialize(new double[] {1.5, -2.2})); }
  @Test void serialize_DoubleArray_returnsFormattedString() { assertEquals("f[2:f1.5:f-2.2]", ValueSerializer.serialize(new Double[] {1.5, -2.2})); }
  @Test void serialize_stringArray_returnsFormattedString() { assertEquals("[2:\"a\":\"b\"]", ValueSerializer.serialize(new String[] {"a", "b"})); }
  @Test void serialize_booleanArray_returnsFormattedString() { assertEquals("b[2:b1:b0]", ValueSerializer.serialize(new boolean[] {true, false})); }
  @Test void serialize_BooleanArray_returnsFormattedString() { assertEquals("b[2:b1:b0]", ValueSerializer.serialize(new Boolean[] {true, false})); }
  @Test void serialize_mixedObjectArray_returnsFormattedString()
  {
    Object[] input = {"Hi", 2};
    assertEquals("[2:\"Hi\":i2]", ValueSerializer.serialize(input));
  }

  @Test void serialize_emptyArray_returnsZeroLength()
  {
    assertEquals("[0]", ValueSerializer.serialize(new Object[] {}));
    assertEquals("i[0]", ValueSerializer.serialize(new int[] {}));
  }

  @Test void serialize_nestedArray_serializesCorrectly()
  {
    Object[] input = {1, new Object[] {2}};
    assertEquals("[2:i1:[1:i2]]", ValueSerializer.serialize(input));
  }

  @Test void serialize_arrayWithNull_handlesNullElements()
  {
    Object[] input = {1, null};
    assertEquals("[2:i1:Z]", ValueSerializer.serialize(input));
  }

  @Test void serialize_char_returnsSingleCharTag() { assertEquals("cL", ValueSerializer.serialize('L')); }
  @Test void serialize_charArray_returnsFormattedString() { assertEquals("c[3:cL:cR:cL]", ValueSerializer.serialize(new char[] {'L', 'R', 'L'})); }

  @Test void serialize_2DIntArray_returnsFormattedString()
  {
    int[][] input = {{1, 2, 3}, {4, 5, 6}};
    assertEquals("[2:i[3:i1:i2:i3]:i[3:i4:i5:i6]]", ValueSerializer.serialize(input));
  }

  @Test void serialize_3DIntArray_returnsFormattedString()
  {
    int[][][] input = {{{1, 2}, {3, 4}}};
    assertEquals("[1:[2:i[2:i1:i2]:i[2:i3:i4]]]", ValueSerializer.serialize(input));
  }

  // ---------------------------------------------------------------
  // deserialize()
  // ---------------------------------------------------------------

  @Test void deserialize_null_returnsNull()
  {
    String input  = "Z";
    Object result = ValueSerializer.deserialize(input);
    assertNull(result);

    assertEquals(input, ValueSerializer.serialize(result));
  }

  @Test void deserialize_mixedArray_withFormatString_mapsCorrectly()
  {
    String input    = "[2:\"Hi:There\":i2]";
    Object[] result = (Object[])ValueSerializer.deserialize(input);
    assertEquals(2, result.length);
    assertEquals("Hi:There", result[0]);
    assertEquals(2, result[1]);

    assertEquals(input, ValueSerializer.serialize(result));
  }

  @Test void deserialize_stringWithEscapedQuotes_parsesCleanly()
  {
    String input    = "[1:\"he\\\"llo\"]";
    Object[] result = (Object[])ValueSerializer.deserialize(input);
    assertEquals(1, result.length);
    assertEquals("he\"llo", result[0]);

    assertEquals(input, ValueSerializer.serialize(result));
  }

  @Test void deserialize_nestedArray_parsesRecursively()
  {
    String input    = "[2:i1:[3:i20:i30:b0]]";
    Object[] result = (Object[])ValueSerializer.deserialize(input);
    assertEquals(2, result.length);
    assertEquals(1, result[0]);

    assertTrue(result[1] instanceof Object[]);
    Object[] nested = (Object[])result[1];
    assertEquals(3, nested.length);
    assertEquals(20, nested[0]);
    assertEquals(30, nested[1]);
    assertEquals(false, nested[2]);

    assertEquals(input, ValueSerializer.serialize(result));
  }

  @Test void deserialize_nestedArray_size1_serializesCorrectly()
  {
    String input    = "[1:[1:i2]]";
    Object[] result = (Object[])ValueSerializer.deserialize(input);
    assertEquals(1, result.length);

    assertTrue(result[0] instanceof Object[]);
    Object[] nested = (Object[])result[0];
    assertEquals(1, nested.length);
    assertEquals(2, nested[0]);

    assertEquals(input, ValueSerializer.serialize(result));
  }

  @Test void deserialize_emptyArray_returnsEmptyObjectArray()
  {
    String input    = "[0]";
    Object[] result = (Object[])ValueSerializer.deserialize(input);
    assertEquals(0, result.length);

    assertEquals(input, ValueSerializer.serialize(result));
  }

  @Test void deserialize_nestedEmptyArray_returnsEmptyObjectArray()
  {
    String input    = "[1:[0]]";
    Object[] result = (Object[])ValueSerializer.deserialize(input);
    assertEquals(1, result.length);

    assertTrue(result[0] instanceof Object[]);
    Object[] nested = (Object[])result[0];
    assertEquals(0, nested.length);

    assertEquals(input, ValueSerializer.serialize(result));
  }

  @Test void deserialize_doubles_parsesCorrectly()
  {
    String input    = "[2:f3.14:f-0.5]";
    Object[] result = (Object[])ValueSerializer.deserialize(input);
    assertEquals(2, result.length);
    assertEquals(3.14, result[0]);
    assertEquals(-0.5, result[1]);

    assertEquals(input, ValueSerializer.serialize(result));
  }

  @Test void deserialize_char_returnsCharacter()
  {
    String input  = "cL";
    Object result = ValueSerializer.deserialize(input);
    assertEquals('L', result);

    assertEquals(input, ValueSerializer.serialize(result));
  }

  @Test void deserialize_charArray_parsesNatively()
  {
    String input  = "c[3:cL:cR:cL]";
    char[] result = (char[])ValueSerializer.deserialize(input);
    assertArrayEquals(new char[] {'L', 'R', 'L'}, result);

    assertEquals(input, ValueSerializer.serialize(result));
  }

  @Test void deserialize_malformedInput_throwsException()
  {
    assertThrows(IllegalArgumentException.class, () -> ValueSerializer.deserialize("[1:i5"));              // Missing closing bracket
    assertThrows(IllegalArgumentException.class, () -> ValueSerializer.deserialize("[1:iA]"));             // Invalid integer
    assertThrows(IllegalArgumentException.class, () -> ValueSerializer.deserialize("[1:\"unterminated]")); // Missing quote
    assertThrows(IllegalArgumentException.class, () -> ValueSerializer.deserialize("[1:x99]"));            // Invalid type hint
  }

  @Test void deserialize_topLevelScalar_returnsScalar() { assertEquals(5, ValueSerializer.deserialize("i5")); }

  // ---------------------------------------------------------------
  // toIntArray()
  // ---------------------------------------------------------------

  @Test void toIntArray_alreadyIntArray_returnsAsIs()
  {
    int[] input = {1, 2, 3};
    assertArrayEquals(input, (int[])ValueSerializer.toIntArray(input));
  }

  @Test void toIntArray_2DArray_rebuildsTypedArray()
  {
    int[][] input       = {{1, 2, 3}, {4, 5, 6}};
    Object deserialized = ValueSerializer.deserialize(ValueSerializer.serialize(input));

    int[][] result = (int[][])ValueSerializer.toIntArray(deserialized);

    assertEquals(2, result.length);
    assertArrayEquals(new int[] {1, 2, 3}, result[0]);
    assertArrayEquals(new int[] {4, 5, 6}, result[1]);
  }

  @Test void toIntArray_3DArray_rebuildsTypedArray()
  {
    // Same shape as HelloTurmiteEntity's rule table: rule[state][currentColor][NEXT_COLOR/MOVE/STATE]
    int[][][] input     = {{{1, 2, 0}, {1, 2, 1}}, {{0, 1, 0}, {0, 1, 1}}};
    Object deserialized = ValueSerializer.deserialize(ValueSerializer.serialize(input));

    int[][][] result = (int[][][])ValueSerializer.toIntArray(deserialized);

    assertEquals(2, result.length);
    assertArrayEquals(new int[] {1, 2, 0}, result[0][0]);
    assertArrayEquals(new int[] {1, 2, 1}, result[0][1]);
    assertArrayEquals(new int[] {0, 1, 0}, result[1][0]);
    assertArrayEquals(new int[] {0, 1, 1}, result[1][1]);
  }

  @Test void toIntArray_emptyArray_rebuildsEmptyTypedArray()
  {
    int[][] input       = {};
    Object deserialized = ValueSerializer.deserialize(ValueSerializer.serialize(input));

    int[][] result = (int[][])ValueSerializer.toIntArray(deserialized);

    assertEquals(0, result.length);
  }
}