#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int diff21(int n);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("i", diff21(params[0].as.i)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
int diff21(int n)
{
  /* BEGIN SOLUTION */
  if (n > 21)
    return 2 * (n - 21);
  return 21 - n;
  /* END SOLUTION */
}
/* END TEMPLATE */
