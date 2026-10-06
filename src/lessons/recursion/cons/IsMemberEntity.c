#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

bool isMember(RecList* seq, int val);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("b", isMember(recListFromValue(args[0]), args[1]->as.i));
    plm_value_free(params);
    free(test);
    recListFreeAll();
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool isMember(RecList* seq, int val)
{
  /* BEGIN SOLUTION */
  if (seq == NULL)
    return false;
  if (seq->head == val)
    return true;
  return isMember(seq->tail, val);
  /* END SOLUTION */
}
/* END TEMPLATE */
