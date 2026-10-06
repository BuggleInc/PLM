#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"

char* stringYak(char* str);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("s", stringYak(args[0]->as.str));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
char* stringYak(char* str)
{
  /* BEGIN SOLUTION */
  int len      = strlen(str);
  char* result = malloc(len + 1);
  int ri       = 0;

  for (int i = 0; i < len; i++) {
    if (i + 2 < len && str[i] == 'y' && str[i + 2] == 'k') {
      i = i + 2;
    } else {
      result[ri++] = str[i];
    }
  }

  result[ri] = '\0';
  return result;
  /* END SOLUTION */
}
/* END TEMPLATE */
