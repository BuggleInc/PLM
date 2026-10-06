#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool in1020(int a, int b);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("b", in1020(params[0].as.i, params[1].as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool in1020(int a, int b)
{
  /* BEGIN SOLUTION */
  return (a > 9 && a < 21) || (b > 9 && b < 21);
  /* END SOLUTION */
}
/* END TEMPLATE */
