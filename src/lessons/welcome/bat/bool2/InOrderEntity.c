#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool inOrder(int a, int b, int c, bool bOk);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("b", inOrder(params[0].as.i, params[1].as.i, params[2].as.i, params[3].as.b));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool inOrder(int a, int b, int c, bool bOk)
{
  /* BEGIN SOLUTION */
  return (bOk || (b > a)) && (c > b);
  /* END SOLUTION */
}
/* END TEMPLATE */
