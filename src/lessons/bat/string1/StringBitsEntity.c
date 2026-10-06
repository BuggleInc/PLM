#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"

char* stringBits(char* str);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("s", stringBits(args[0]->as.str));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
char* stringBits(char* str)
{
  /* BEGIN SOLUTION */
  int len      = strlen(str);
  char* result = malloc(len / 2 + 2);
  int ri       = 0;
  for (int i = 0; i < len; i += 2)
    result[ri++] = str[i];
  result[ri] = '\0';
  return result;
  /* END SOLUTION */
}
/* END TEMPLATE */
