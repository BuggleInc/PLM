#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool shareDigit(int a, int b);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("b", shareDigit(params[0].as.i, params[1].as.i)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
bool shareDigit(int a, int b)
{
  /* BEGIN SOLUTION */
  return (a / 10 == b / 10 || a / 10 == b % 10 || a % 10 == b / 10 || a % 10 == b % 10);
  /* END SOLUTION */
}
/* END TEMPLATE */
