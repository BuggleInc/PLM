#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int sortaSum(int a, int b);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", sortaSum(args[0]->as.i, args[1]->as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int sortaSum(int a, int b)
{
  /* BEGIN SOLUTION */
  int sum = a + b;
  if (sum >= 10 && sum <= 19)
    return 20;
  else
    return sum;
  /* END SOLUTION */
}
/* END TEMPLATE */
