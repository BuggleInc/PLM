#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int maxMod5(int a, int b);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("i", maxMod5(params[0].as.i, params[1].as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int maxMod5(int a, int b)
{
  /* BEGIN SOLUTION */
  if (a == b)
    return 0;
  else if (a > b)
    if (a % 5 == b % 5)
      return b;
    else
      return a;
  else if (a % 5 == b % 5)
    return a;
  else
    return b;
  /* END SOLUTION */
}
/* END TEMPLATE */
