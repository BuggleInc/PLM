#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int teenSum(int a, int b);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("i", teenSum(params[0].as.i, params[1].as.i)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
int teenSum(int a, int b)
{
  /* BEGIN SOLUTION */
  if ((a >= 13 && a <= 19) || (b >= 13 && b <= 19))
    return 19;
  else
    return a + b;
  /* END SOLUTION */
}
/* END TEMPLATE */
