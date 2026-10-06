#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int sumDouble(int a, int b);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("i", sumDouble(params[0].as.i, params[1].as.i)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
int sumDouble(int a, int b)
{
  /* BEGIN SOLUTION */
  if (a == b)
    return (a + b) * 2;
  return a + b;
  /* END SOLUTION */
}
/* END TEMPLATE */
