#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"

int last2(char* str);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("i", last2(params[0].as.str));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int last2(char* str)
{
  /* BEGIN SOLUTION */
  int len = strlen(str);
  if (len < 2)
    return 0;

  char* end = str + len - 2;
  int count = 0;

  for (int i = 0; i < len - 2; i++) {
    if (strncmp(str + i, end, 2) == 0)
      count++;
  }

  return count;
  /* END SOLUTION */
}
/* END TEMPLATE */
