#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool monkeyTrouble(bool aSmile, bool bSmile);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("b", monkeyTrouble(params[0].as.b, params[1].as.b)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
bool monkeyTrouble(bool aSmile, bool bSmile)
{
  /* BEGIN SOLUTION */
  if (aSmile && bSmile) {
    return true;
  }
  if (!aSmile && !bSmile) {
    return true;
  }
  return false;
  // This all can be shortened to just:
  // return ((aSmile && bSmile) || (!aSmile && !bSmile));
  /* END SOLUTION */
}
/* END TEMPLATE */
