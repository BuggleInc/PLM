#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int close10(int a, int b);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("i", close10(params[0].as.i, params[1].as.i)));
    plm_value_free(params);
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
