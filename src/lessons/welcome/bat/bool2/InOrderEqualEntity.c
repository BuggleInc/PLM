#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool inOrderEqual(int a, int b, int c, bool equalOk);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("b", inOrderEqual(params[0].as.i, params[1].as.i, params[2].as.i, params[3].as.b)));
    plm_value_free(params);
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
