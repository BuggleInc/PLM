#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"

char* stringX(char* str);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("s", stringX(args[0]->as.str));
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
char* stringX(char* str)
{
  /* BEGIN SOLUTION */
  int len      = strlen(str);
  char* result = malloc(len + 1);
  int ri       = 0;
  for (int i = 0; i < len; i++) {
    if (!(i > 0 && i < (len - 1) && str[i] == 'x')) {
      result[ri++] = str[i];
    }
  }
  result[ri] = '\0';
  return result;
  /* END SOLUTION */
}
/* END TEMPLATE */
