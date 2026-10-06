#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool answerCell(bool isMorning, bool isMom, bool isAsleep);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("b", answerCell(args[0]->as.b, args[1]->as.b, args[2]->as.b));
    plm_value_free(params);
    free(test);
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
