#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"

int stringMatch(char* a, char* b);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", stringMatch(args[0]->as.str, args[1]->as.str));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int stringMatch(char* a, char* b)
{
  /* BEGIN SOLUTION */
  int lenA = strlen(a), lenB = strlen(b);
  int len   = lenA < lenB ? lenA : lenB;
  int count = 0;

  for (int i = 0; i < len - 1; i++) {
    if (strncmp(a + i, b + i, 2) == 0)
      count++;
  }

  return count;
  /* END SOLUTION */
}
/* END TEMPLATE */
