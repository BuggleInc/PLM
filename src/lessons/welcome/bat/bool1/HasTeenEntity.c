#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool hasTeen(int a, int b, int c);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("b", hasTeen(params[0].as.i, params[1].as.i, params[2].as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool hasTeen(int a, int b, int c)
{
  /* BEGIN SOLUTION */
  return (a > 12 && a < 20) || (b > 12 && b < 20) || (c > 12 && c < 20);
  /* END SOLUTION */
}
/* END TEMPLATE */
