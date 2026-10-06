#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"

char* stringTimes(char* str, int n);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("s", stringTimes(args[0]->as.str, args[1]->as.i));
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
char* stringTimes(char* str, int n)
{
  /* BEGIN SOLUTION */
  char* result = malloc(strlen(str) * n + 1);
  result[0]    = '\0';
  for (int i = 0; i < n; i++)
    strcat(result, str);
  return result;
  /* END SOLUTION */
}
/* END TEMPLATE */
