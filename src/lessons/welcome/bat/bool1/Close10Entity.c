#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int close10(int a, int b);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", close10(args[0]->as.i, args[1]->as.i));
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int close10(int a, int b)
{
  /* BEGIN SOLUTION */
  if (abs(a - 10) == abs(b - 10))
    return 0;
  if (abs(a - 10) < abs(b - 10))
    return a;
  return b;
  /* END SOLUTION */
}
/* END TEMPLATE */
