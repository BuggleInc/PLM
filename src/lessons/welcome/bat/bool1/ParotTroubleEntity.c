#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool parotTrouble(bool talking, int hour);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("b", parotTrouble(params[0].as.b, params[1].as.i)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
bool parotTrouble(bool talking, int hour)
{
  /* BEGIN SOLUTION */
  return (talking && (hour < 7 || hour > 20));
  /* END SOLUTION */
}
/* END TEMPLATE */
