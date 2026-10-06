#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool monkeyTrouble(bool aSmile, bool bSmile);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("b", monkeyTrouble(args[0]->as.b, args[1]->as.b));
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
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
