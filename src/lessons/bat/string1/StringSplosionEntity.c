#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"

char* stringSplosion(char* str);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* result        = stringSplosion(args[0]->as.str);
    char* serialized    = plm_serialize_fmt("s", result);
    free(result);
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
char* stringSplosion(char* str)
{
  /* BEGIN SOLUTION */
  int len      = strlen(str);
  char* result = malloc(len * (len + 1) / 2 + 1);
  result[0]    = '\0';
  for (int i = 0; i < len; i++) {
    strncat(result, str, i + 1);
  }
  return result;
  /* END SOLUTION */
}
/* END TEMPLATE */
