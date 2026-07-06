package plm.universe.bat;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import lessons.recursion.cons.universe.RecList;
import org.junit.jupiter.api.Test;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;

/** Tests for ValueFormatter */
class ValueFormatterTest {

  // ---------------------------------------------------------------
  // equals() - scalars
  // ---------------------------------------------------------------

  @Test void equals_bothNull_isTrue() { assertTrue(ValueFormatter.equals(null, null)); }

  @Test void equals_oneNull_isFalse()
  {
    assertFalse(ValueFormatter.equals(null, 5));
    assertFalse(ValueFormatter.equals(5, null));
  }

  @Test void equals_equalScalars_isTrue()
  {
    assertTrue(ValueFormatter.equals(3, 3));
    assertTrue(ValueFormatter.equals("a", "a"));
  }

  @Test void equals_differentScalars_isFalse()
  {
    assertFalse(ValueFormatter.equals(3, 4));
    assertFalse(ValueFormatter.equals("a", "b"));
  }

  // ---------------------------------------------------------------
  // equals() - integer arrays, across the accepted input shapes
  // ---------------------------------------------------------------

  @Test void equals_identicalIntArrays_isTrue()
  {
    assertTrue(ValueFormatter.equals(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
  }

  @Test void equals_differentValues_isFalse()
  {
    assertFalse(ValueFormatter.equals(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
  }

  @Test void equals_differentLength_isFalse()
  {
    assertFalse(ValueFormatter.equals(new int[] {1, 2}, new int[] {1, 2, 3}));
  }

  @Test void equals_listOfIntegerVsIntArray_isTrue()
  {
    List<Integer> list = Arrays.asList(1, 2, 3);
    assertTrue(ValueFormatter.equals(list, new int[] {1, 2, 3}));
  }

  @Test void equals_integerArrayVsIntArray_isTrue()
  {
    assertTrue(ValueFormatter.equals(new Integer[] {1, 2, 3}, new int[] {1, 2, 3}));
  }

  @Test void equals_recListVsIntArray_isTrue()
  {
    RecList rl = RecList.fromArray(new int[] {1, 2, 3});
    assertTrue(ValueFormatter.equals(rl, new int[] {1, 2, 3}));
  }

  @Test void equals_identicalStringArrays_isTrue()
  {
    assertTrue(ValueFormatter.equals(new String[] {"x", "y"}, new String[] {"x", "y"}));
  }

  // ---------------------------------------------------------------
  // format() - null per language
  // ---------------------------------------------------------------

  @Test void format_null_java() { assertEquals("null", ValueFormatter.format(null, javaLang())); }

  @Test void format_null_scala() { assertEquals("Nil", ValueFormatter.format(null, scalaLang())); }

  @Test void format_null_python() { assertEquals("None", ValueFormatter.format(null, pythonLang())); }

  // ---------------------------------------------------------------
  // format() - int[] per language
  // ---------------------------------------------------------------

  @Test void format_intArray_java() { assertEquals("{1,2,3}", ValueFormatter.format(new int[] {1, 2, 3}, javaLang())); }

  @Test void format_intArray_scala()
  {
    assertEquals("Array(1,2,3)", ValueFormatter.format(new int[] {1, 2, 3}, scalaLang()));
  }

  @Test void format_intArray_python()
  {
    assertEquals("[1,2,3]", ValueFormatter.format(new int[] {1, 2, 3}, pythonLang()));
  }

  @Test void format_emptyIntArray_noTrailingComma()
  {
    assertEquals("{}", ValueFormatter.format(new int[] {}, javaLang()));
  }

  // ---------------------------------------------------------------
  // format() - String[]
  // ---------------------------------------------------------------

  @Test void format_stringArray_java()
  {
    assertEquals("{a,b}", ValueFormatter.format(new String[] {"a", "b"}, javaLang()));
  }

  @Test void format_stringArray_python()
  {
    assertEquals("[a,b]", ValueFormatter.format(new String[] {"a", "b"}, pythonLang()));
  }

  // ---------------------------------------------------------------
  // format() - Boolean
  // ---------------------------------------------------------------

  @Test void format_boolean_javaAndScala_useLowercase()
  {
    assertEquals("true", ValueFormatter.format(Boolean.TRUE, javaLang()));
    assertEquals("false", ValueFormatter.format(Boolean.FALSE, scalaLang()));
  }

  @Test void format_boolean_python_usesCapitalized()
  {
    assertEquals("True", ValueFormatter.format(Boolean.TRUE, pythonLang()));
    assertEquals("False", ValueFormatter.format(Boolean.FALSE, pythonLang()));
  }

  // ---------------------------------------------------------------
  // format() - String scalars
  // ---------------------------------------------------------------

  @Test void format_string_isQuoted()
  {
    assertEquals("\"hi\"", ValueFormatter.format("hi", javaLang()));
    assertEquals("\"hi\"", ValueFormatter.format("hi", pythonLang()));
    assertEquals("\"hi\"", ValueFormatter.format("hi", scalaLang()));
  }

  // ---------------------------------------------------------------
  // format() - Vector<Integer>
  // ---------------------------------------------------------------

  @Test void format_vectorOfInteger_isNormalizedThenFormatted()
  {
    Vector<Integer> vec = new Vector<>();
    vec.add(9);
    vec.add(8);
    assertEquals("{9,8}", ValueFormatter.format(vec, javaLang()));
  }

  // ---------------------------------------------------------------
  // normalize() - passthrough / idempotence
  // ---------------------------------------------------------------

  @Test void normalize_null_isNull() { assertNull(ValueFormatter.normalize(null)); }

  @Test void normalize_intArray_isUnchanged()
  {
    int[] input = {7, 8};
    assertArrayEquals(input, (int[])ValueFormatter.normalize(input));
  }

  @Test void normalize_plainScalar_isUnchanged() { assertEquals(5, ValueFormatter.normalize(5)); }

  // ---------------------------------------------------------------
  // serialize()
  // ---------------------------------------------------------------

  @Test void serialize_null_returnsNullString() { assertEquals("Z", ValueFormatter.serialize(null)); }

  @Test void serialize_boolean_returnsOneOrZero()
  {
    assertEquals("b1", ValueFormatter.serialize(true));
    assertEquals("b0", ValueFormatter.serialize(false));
  }

  @Test void serialize_string_isQuotedAndEscaped()
  {
    assertEquals("\"hello\"", ValueFormatter.serialize("hello"));
    assertEquals("\"he\\\"llo\"", ValueFormatter.serialize("he\"llo"));
  }

  @Test void serialize_intArray_returnsFormattedString() { assertEquals("[3:i1:i2:i3]", ValueFormatter.serialize(new int[] {1, 2, 3})); }
  @Test void serialize_IntegerArray_returnsFormattedString() { assertEquals("[2:i1:i2]", ValueFormatter.serialize(new Integer[] {1, 2})); }

  @Test void serialize_doubleArray_returnsFormattedString() { assertEquals("[2:f1.5:f-2.2]", ValueFormatter.serialize(new double[] {1.5, -2.2})); }
  @Test void serialize_DoubleArray_returnsFormattedString() { assertEquals("[2:f1.5:f-2.2]", ValueFormatter.serialize(new Double[] {1.5, -2.2})); }
  @Test void serialize_stringArray_returnsFormattedString() { assertEquals("[2:\"a\":\"b\"]", ValueFormatter.serialize(new String[] {"a", "b"})); }
  @Test void serialize_booleanArray_returnsFormattedString() { assertEquals("[2:b1:b0]", ValueFormatter.serialize(new boolean[] {true, false})); }
  @Test void serialize_BooleanArray_returnsFormattedString() { assertEquals("[2:b1:b0]", ValueFormatter.serialize(new Boolean[] {true, false})); }
  @Test void serialize_mixedObjectArray_returnsFormattedString()
  {
    Object[] input = {"Hi", 2};
    assertEquals("[2:\"Hi\":i2]", ValueFormatter.serialize(input));
  }

  @Test void serialize_emptyArray_returnsZeroLength()
  {
    assertEquals("[0]", ValueFormatter.serialize(new Object[] {}));
    assertEquals("[0]", ValueFormatter.serialize(new int[] {}));
  }

  @Test void serialize_nestedArray_serializesCorrectly()
  {
    Object[] input = {1, new Object[] {2}};
    assertEquals("[2:i1:[1:i2]]", ValueFormatter.serialize(input));
  }

  @Test void serialize_arrayWithNull_handlesNullElements()
  {
    Object[] input = {1, null};
    assertEquals("[2:i1:Z]", ValueFormatter.serialize(input));
  }

  // ---------------------------------------------------------------
  // deserialize()
  // ---------------------------------------------------------------

  @Test void deserialize_null_returnsNull()
  {
    String input  = "Z";
    Object result = ValueFormatter.deserialize(input);
    assertNull(result);

    assertEquals(input, ValueFormatter.serialize(result));
  }

  @Test void deserialize_mixedArray_withFormatString_mapsCorrectly()
  {
    String input    = "[2:\"Hi:There\":i2]";
    Object[] result = (Object[])ValueFormatter.deserialize(input);
    assertEquals(2, result.length);
    assertEquals("Hi:There", result[0]);
    assertEquals(2, result[1]);

    assertEquals(input, ValueFormatter.serialize(result));
  }

  @Test void deserialize_stringWithEscapedQuotes_parsesCleanly()
  {
    String input    = "[1:\"he\\\"llo\"]";
    Object[] result = (Object[])ValueFormatter.deserialize(input);
    assertEquals(1, result.length);
    assertEquals("he\"llo", result[0]);

    assertEquals(input, ValueFormatter.serialize(result));
  }

  @Test void deserialize_nestedArray_parsesRecursively()
  {
    String input    = "[2:i1:[3:i20:i30:b0]]";
    Object[] result = (Object[])ValueFormatter.deserialize(input);
    assertEquals(2, result.length);
    assertEquals(1, result[0]);

    assertTrue(result[1] instanceof Object[]);
    Object[] nested = (Object[])result[1];
    assertEquals(3, nested.length);
    assertEquals(20, nested[0]);
    assertEquals(30, nested[1]);
    assertEquals(false, nested[2]);

    assertEquals(input, ValueFormatter.serialize(result));
  }

  @Test void deserialize_emptyArray_returnsEmptyObjectArray()
  {
    String input    = "[0]";
    Object[] result = (Object[])ValueFormatter.deserialize("[0]");
    assertEquals(0, result.length);

    assertEquals(input, ValueFormatter.serialize(result));
  }

  @Test void deserialize_doubles_parsesCorrectly()
  {
    String input    = "[2:f3.14:f-0.5]";
    Object[] result = (Object[])ValueFormatter.deserialize(input);
    assertEquals(2, result.length);
    assertEquals(3.14, result[0]);
    assertEquals(-0.5, result[1]);

    assertEquals(input, ValueFormatter.serialize(result));
  }

  @Test void deserialize_malformedInput_throwsException()
  {
    assertThrows(IllegalArgumentException.class, () -> ValueFormatter.deserialize("[1:i5"));              // Missing closing bracket
    assertThrows(IllegalArgumentException.class, () -> ValueFormatter.deserialize("[1:iA]"));             // Invalid integer
    assertThrows(IllegalArgumentException.class, () -> ValueFormatter.deserialize("[1:\"unterminated]")); // Missing quote
    assertThrows(IllegalArgumentException.class, () -> ValueFormatter.deserialize("[1:x99]"));            // Invalid type hint
  }

  @Test void deserialize_topLevelScalar_returnsScalar() { assertEquals(5, ValueFormatter.deserialize("i5")); }

  // ---------------------------------------------------------------
  // Helpers - shorten how to retrive the programming language
  // ---------------------------------------------------------------

  private ProgrammingLanguage javaLang() { return Game.getInstance().programmingLanguageManager.JAVA; }
  private ProgrammingLanguage scalaLang() { return Game.getInstance().programmingLanguageManager.SCALA; }
  private ProgrammingLanguage pythonLang() { return Game.getInstance().programmingLanguageManager.PYTHON; }
}
