#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"

char* stringTimes(char* str, int n);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("s", stringTimes(params[0].as.str, params[1].as.i)));
    plm_value_free(params);
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
