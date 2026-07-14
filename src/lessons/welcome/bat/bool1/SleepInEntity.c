#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool sleepIn(bool weekday, bool vacation);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("b", sleepIn(params[0].as.b, params[1].as.b)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
bool sleepIn(bool weekday, bool vacation)
{
  /* BEGIN SOLUTION */
  return !weekday || vacation;
  /* END SOLUTION */
}
/* END TEMPLATE */
