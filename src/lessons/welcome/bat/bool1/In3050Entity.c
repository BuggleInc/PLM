#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool in3050(int a, int b);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("b", in3050(params[0].as.i, params[1].as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool in3050(int a, int b)
{
  /* BEGIN SOLUTION */
  return (a > 29 && a < 41 && b > 29 && b < 41) || (a > 39 && a < 51 && b > 39 && b < 51);
  /* END SOLUTION */
}
/* END TEMPLATE */
