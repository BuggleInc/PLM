#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"

char* frontTimes(char* str, int n);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("s", frontTimes(params[0].as.str, params[1].as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
char* frontTimes(char* str, int n)
{
  /* BEGIN SOLUTION */
  int frontLen = 3;
  int len      = strlen(str);
  if (frontLen > len)
    frontLen = len;
  char front[4];
  strncpy(front, str, frontLen);
  front[frontLen] = '\0';

  char* result = malloc(frontLen * n + 1);
  result[0]    = '\0';
  for (int i = 0; i < n; i++)
    strcat(result, front);
  return result;
  /* END SOLUTION */
}
/* END TEMPLATE */
