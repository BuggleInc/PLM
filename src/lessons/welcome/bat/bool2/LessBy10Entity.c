#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool lessBy10(int a, int b, int c);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("b", lessBy10(args[0]->as.i, args[1]->as.i, args[2]->as.i));
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool lessBy10(int a, int b, int c)
{
  /* BEGIN SOLUTION */
  return ((a - b) >= 10) || ((b - a) >= 10) || ((b - c) >= 10) || ((c - b) >= 10) || ((a - c) >= 10) || ((c - a) >= 10);
  /* END SOLUTION */
}
/* END TEMPLATE */
