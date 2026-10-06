#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int max1020(int a, int b);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", max1020(args[0]->as.i, args[1]->as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int max1020(int a, int b)
{
  /* BEGIN SOLUTION */
  int A = a > b ? a : b;
  int B = a > b ? b : a;
  if (A < 21 && A > 9)
    return A;
  if (B < 21 && B > 9)
    return B;
  return 0;
  /* END SOLUTION */
}
/* END TEMPLATE */
