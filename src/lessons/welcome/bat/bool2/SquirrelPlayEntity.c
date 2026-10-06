#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool squirrelPlay(int temp, bool isSummer);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("b", squirrelPlay(params[0].as.i, params[1].as.b)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
bool squirrelPlay(int temp, bool isSummer)
{
  /* BEGIN SOLUTION */
  return (temp >= 60 && ((isSummer && temp <= 100) || temp <= 90));
  /* END SOLUTION */
}
/* END TEMPLATE */
