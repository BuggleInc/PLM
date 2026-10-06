#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool answerCell(bool isMorning, bool isMom, bool isAsleep);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("b", answerCell(params[0].as.b, params[1].as.b, params[2].as.b));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool answerCell(bool isMorning, bool isMom, bool isAsleep)
{
  /* BEGIN SOLUTION */
  return (!isAsleep) && !(isMorning && !isMom);
  /* END SOLUTION */
}
/* END TEMPLATE */
