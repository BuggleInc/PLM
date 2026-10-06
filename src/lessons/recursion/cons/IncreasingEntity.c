#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

bool increasing(RecList* seq);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("b", increasing(recListFromValue(args[0])));
    plm_value_free(params);
    free(test);
    recListFreeAll();
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool increasing(RecList* seq)
{
  /* BEGIN SOLUTION */
  if (seq == NULL || seq->tail == NULL)
    return true;
  if (seq->head > seq->tail->head)
    return false;
  return increasing(seq->tail);
  /* END SOLUTION */
}
/* END TEMPLATE */
