#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool posNeg(int a, int b, bool negative);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("b", posNeg(params[0].as.i, params[1].as.i, params[2].as.b)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
bool posNeg(int a, int b, bool negative)
{
  /* BEGIN SOLUTION */
  if (negative)
    return a < 0 && b < 0;
  return (a < 0 && b > 0) || (a > 0 && b < 0);
  /* END SOLUTION */
}
/* END TEMPLATE */
