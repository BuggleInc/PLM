#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool twoAsOne(int a, int b, int c);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("b", twoAsOne(params[0].as.i, params[1].as.i, params[2].as.i)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
bool twoAsOne(int a, int b, int c)
{
  /* BEGIN SOLUTION */
  return (a + b == c) || (a + c == b) || (b + c == a);
  /* END SOLUTION */
}
/* END TEMPLATE */
