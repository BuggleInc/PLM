#include "simple_unit_test.h"
#include "value_serializer.h"

// --- serialize() Tests ---

SUT_TEST(serialize_null_returnsNullString)
{
  char* str = plm_serialize_fmt("Z");
  SUT_STR_EQUAL("Z", str);
  free(str);
  return 1;
}

SUT_TEST(serialize_boolean_returnsOneOrZero)
{
  char* str1 = plm_serialize_fmt("b", true);
  SUT_STR_EQUAL("b1", str1);
  free(str1);

  char* str2 = plm_serialize_fmt("b", false);
  SUT_STR_EQUAL("b0", str2);
  free(str2);
  return 1;
}

SUT_TEST(serialize_string_isQuotedAndEscaped)
{
  char* str1 = plm_serialize_fmt("s", "hello");
  SUT_STR_EQUAL("\"hello\"", str1);
  free(str1);

  char* str2 = plm_serialize_fmt("s", "he\"llo");
  SUT_STR_EQUAL("\"he\\\"llo\"", str2);
  free(str2);
  return 1;
}

SUT_TEST(serialize_intArray_returnsFormattedString)
{
  char* str = plm_serialize_fmt("[iii]", 1, 2, 3);
  SUT_STR_EQUAL("i[3:i1:i2:i3]", str);
  free(str);
  return 1;
}

SUT_TEST(serialize_mixedObjectArray_returnsFormattedString)
{
  char* str = plm_serialize_fmt("[si]", "Hi", 2);
  SUT_STR_EQUAL("[2:\"Hi\":i2]", str);
  free(str);
  return 1;
}

SUT_TEST(serialize_emptyArray_returnsZeroLength)
{
  char* str = plm_serialize_fmt("[]");
  SUT_STR_EQUAL("[0]", str);
  free(str);
  return 1;
}

SUT_TEST(serialize_nestedArray_serializesCorrectly)
{
  char* str = plm_serialize_fmt("[i[i]]", 1, 2);
  SUT_STR_EQUAL("[2:i1:i[1:i2]]", str);
  free(str);
  return 1;
}

SUT_TEST(serialize_opaqueColor_returnsPackedArgbInt)
{
  char* str = plm_serialize_fmt("C", 0xFFFF0000);
  SUT_STR_EQUAL("C-65536", str);
  free(str);
  return 1;
}

SUT_TEST(serialize_2DIntArray_returnsFormattedString)
{
  char* str = plm_serialize_fmt("[[iii][iii]]", 1, 2, 3, 4, 5, 6);
  SUT_STR_EQUAL("[2:i[3:i1:i2:i3]:i[3:i4:i5:i6]]", str);
  free(str);
  return 1;
}

SUT_TEST(serialize_3DIntArray_returnsFormattedString)
{
  char* str = plm_serialize_fmt("[[[ii][ii]]]", 1, 2, 3, 4);
  SUT_STR_EQUAL("[1:[2:i[2:i1:i2]:i[2:i3:i4]]]", str);
  free(str);
  return 1;
}

// --- deserialize() Tests ---

SUT_TEST(deserialize_null_returnsNull)
{
  plm_value_t* val = plm_deserialize("Z");
  SUT_ASSERT_TRUE(val != NULL);
  SUT_INT_EQUAL(PLM_VAL_NULL, val->type);
  plm_value_free(val);
  return 1;
}

SUT_TEST(deserialize_mixedArray_withFormatString_mapsCorrectly)
{
  plm_value_t* val = plm_deserialize("[2:\"Hi:There\":i2]");
  SUT_ASSERT_TRUE(val != NULL);
  SUT_INT_EQUAL(PLM_VAL_ARRAY, val->type);
  SUT_INT_EQUAL(2, val->as.array.size);
  SUT_STR_EQUAL("Hi:There", val->as.array.elements[0]->as.str);
  SUT_INT_EQUAL(2, val->as.array.elements[1]->as.i);
  plm_value_free(val);
  return 1;
}

SUT_TEST(deserialize_nestedArray_parsesRecursively)
{
  plm_value_t* val = plm_deserialize("[2:i1:[3:i20:i30:b0]]");
  SUT_ASSERT_TRUE(val != NULL);
  SUT_INT_EQUAL(2, val->as.array.size);
  SUT_INT_EQUAL(1, val->as.array.elements[0]->as.i);

  plm_value_t* nested = val->as.array.elements[1];
  SUT_INT_EQUAL(PLM_VAL_ARRAY, nested->type);
  SUT_INT_EQUAL(3, nested->as.array.size);
  SUT_INT_EQUAL(30, nested->as.array.elements[1]->as.i);
  SUT_ASSERT_FALSE(nested->as.array.elements[2]->as.b);
  plm_value_free(val);
  return 1;
}

SUT_TEST(deserialize_doubles_parsesCorrectly)
{
  plm_value_t* val = plm_deserialize("[2:f3.14:f-0.5]");
  SUT_ASSERT_TRUE(val != NULL);
  SUT_DOUBLE_EQUAL(3.14, val->as.array.elements[0]->as.f);
  SUT_DOUBLE_EQUAL(-0.5, val->as.array.elements[1]->as.f);
  plm_value_free(val);
  return 1;
}

SUT_TEST(deserialize_char_returnsCharacter)
{
  plm_value_t* val = plm_deserialize("cL");
  SUT_ASSERT_TRUE(val != NULL);
  SUT_CHAR_EQUAL('L', val->as.c);
  plm_value_free(val);
  return 1;
}

SUT_TEST(deserialize_malformedInput_throwsException)
{
  SUT_ASSERT_TRUE(plm_deserialize("[1:i5") == NULL);
  SUT_ASSERT_TRUE(plm_deserialize("[1:iA]") == NULL);
  SUT_ASSERT_TRUE(plm_deserialize("[1:\"unterminated]") == NULL);
  return 1;
}

// --- toIntArray Native Conversions Tests ---

SUT_TEST(toIntArray_2DArray_rebuildsTypedArray)
{
  plm_value_t* val = plm_deserialize("[2:i[3:i1:i2:i3]:i[3:i4:i5:i6]]");
  uint32_t** arr   = plm_to_int_array_2d(val);

  SUT_INT_EQUAL(1, arr[0][0]);
  SUT_INT_EQUAL(3, arr[0][2]);
  SUT_INT_EQUAL(4, arr[1][0]);
  SUT_INT_EQUAL(6, arr[1][2]);

  plm_free_int_array_2d(arr, 2);
  plm_value_free(val);
  return 1;
}

SUT_TEST(toIntArray_3DArray_rebuildsTypedArray)
{
  plm_value_t* val = plm_deserialize("[1:[2:i[2:i1:i2]:i[2:i3:i4]]]");
  uint32_t*** arr  = plm_to_int_array_3d(val);

  SUT_INT_EQUAL(1, arr[0][0][0]);
  SUT_INT_EQUAL(2, arr[0][0][1]);
  SUT_INT_EQUAL(3, arr[0][1][0]);
  SUT_INT_EQUAL(4, arr[0][1][1]);

  plm_free_int_array_3d(arr, 1, 2);
  plm_value_free(val);
  return 1;
}

SUT_TEST_SUITE(ValueSerializerTest) = {SUT_TEST_SUITE_ADD(serialize_null_returnsNullString),
                                       SUT_TEST_SUITE_ADD(serialize_boolean_returnsOneOrZero),
                                       SUT_TEST_SUITE_ADD(serialize_string_isQuotedAndEscaped),
                                       SUT_TEST_SUITE_ADD(serialize_intArray_returnsFormattedString),
                                       SUT_TEST_SUITE_ADD(serialize_mixedObjectArray_returnsFormattedString),
                                       SUT_TEST_SUITE_ADD(serialize_emptyArray_returnsZeroLength),
                                       SUT_TEST_SUITE_ADD(serialize_nestedArray_serializesCorrectly),
                                       SUT_TEST_SUITE_ADD(serialize_opaqueColor_returnsPackedArgbInt),
                                       SUT_TEST_SUITE_ADD(serialize_2DIntArray_returnsFormattedString),
                                       SUT_TEST_SUITE_ADD(serialize_3DIntArray_returnsFormattedString),
                                       SUT_TEST_SUITE_ADD(deserialize_null_returnsNull),
                                       SUT_TEST_SUITE_ADD(deserialize_mixedArray_withFormatString_mapsCorrectly),
                                       SUT_TEST_SUITE_ADD(deserialize_nestedArray_parsesRecursively),
                                       SUT_TEST_SUITE_ADD(deserialize_doubles_parsesCorrectly),
                                       SUT_TEST_SUITE_ADD(deserialize_char_returnsCharacter),
                                       SUT_TEST_SUITE_ADD(deserialize_malformedInput_throwsException),
                                       SUT_TEST_SUITE_ADD(toIntArray_2DArray_rebuildsTypedArray),
                                       SUT_TEST_SUITE_ADD(toIntArray_3DArray_rebuildsTypedArray),
                                       SUT_TEST_SUITE_END};

SUT_DECLARE_MAIN_FUNC