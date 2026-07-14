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

  @Test void equals_identicalIntArrays_isTrue() { assertTrue(ValueFormatter.equals(new int[] {1, 2, 3}, new int[] {1, 2, 3})); }

  @Test void equals_differentValues_isFalse() { assertFalse(ValueFormatter.equals(new int[] {1, 2, 3}, new int[] {1, 2, 4})); }

  @Test void equals_differentLength_isFalse() { assertFalse(ValueFormatter.equals(new int[] {1, 2}, new int[] {1, 2, 3})); }

  @Test void equals_listOfIntegerVsIntArray_isTrue()
  {
    List<Integer> list = Arrays.asList(1, 2, 3);
    assertTrue(ValueFormatter.equals(list, new int[] {1, 2, 3}));
  }

  @Test void equals_integerArrayVsIntArray_isTrue() { assertTrue(ValueFormatter.equals(new Integer[] {1, 2, 3}, new int[] {1, 2, 3})); }

  @Test void equals_recListVsIntArray_isTrue()
  {
    RecList rl = RecList.fromArray(new int[] {1, 2, 3});
    assertTrue(ValueFormatter.equals(rl, new int[] {1, 2, 3}));
  }

  @Test void equals_identicalStringArrays_isTrue() { assertTrue(ValueFormatter.equals(new String[] {"x", "y"}, new String[] {"x", "y"})); }

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

  @Test void format_intArray_scala() { assertEquals("Array(1,2,3)", ValueFormatter.format(new int[] {1, 2, 3}, scalaLang())); }

  @Test void format_intArray_python() { assertEquals("[1,2,3]", ValueFormatter.format(new int[] {1, 2, 3}, pythonLang())); }

  @Test void format_emptyIntArray_noTrailingComma() { assertEquals("{}", ValueFormatter.format(new int[] {}, javaLang())); }

  // ---------------------------------------------------------------
  // format() - String[]
  // ---------------------------------------------------------------

  @Test void format_stringArray_java() { assertEquals("{a,b}", ValueFormatter.format(new String[] {"a", "b"}, javaLang())); }

  @Test void format_stringArray_python() { assertEquals("[a,b]", ValueFormatter.format(new String[] {"a", "b"}, pythonLang())); }

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
  // Helpers - shorten how to retrive the programming language
  // ---------------------------------------------------------------

  private ProgrammingLanguage javaLang() { return Game.getInstance().programmingLanguageManager.JAVA; }
  private ProgrammingLanguage scalaLang() { return Game.getInstance().programmingLanguageManager.SCALA; }
  private ProgrammingLanguage pythonLang() { return Game.getInstance().programmingLanguageManager.PYTHON; }
}
