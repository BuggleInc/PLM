#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool inOrderEqual(int a, int b, int c, bool equalOk);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("b", inOrderEqual(args[0]->as.i, args[1]->as.i, args[2]->as.i, args[3]->as.b));
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool inOrderEqual(int a, int b, int c, bool equalOk)
{
  /* BEGIN SOLUTION */
  return (equalOk && ((a <= b) && (b <= c))) || (a < b && b < c);
  /* END SOLUTION */
}
/* END TEMPLATE */
