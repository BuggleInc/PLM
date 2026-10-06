#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool icyHot(int temp1, int temp2);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("b", icyHot(params[0].as.i, params[1].as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool icyHot(int temp1, int temp2)
{
  /* BEGIN SOLUTION */
  return (temp1 < 0 && temp2 > 100) || (temp1 > 100 && temp2 < 0);
  /* END SOLUTION */
}
/* END TEMPLATE */
