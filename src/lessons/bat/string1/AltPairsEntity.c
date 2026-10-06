#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"

char* altPairs(char* str);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("s", altPairs(args[0]->as.str));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
char* altPairs(char* str)
{
  /* BEGIN SOLUTION */
  int len   = strlen(str);
  char* res = malloc(len + 1);
  int ri    = 0;
  for (int i = 0; i < len; i += 4) {
    int end = i + 2;
    if (end > len)
      end = len;
    for (int j = i; j < end; j++)
      res[ri++] = str[j];
  }
  res[ri] = '\0';
  return res;
  /* END SOLUTION */
}
/* END TEMPLATE */
