#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

bool allDifferent(RecList* seq);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("b", allDifferent(recListFromValue(args[0])));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool allDifferent(RecList* seq)
{
  /* BEGIN SOLUTION */
  if (seq == NULL)
    return true;
  /* inline compute isMember */
  RecList* ptr = seq->tail;
  while (ptr != NULL && ptr->head != seq->head)
    ptr = ptr->tail;
  if (ptr != NULL)
    return false;
  /* end isMember */
  return allDifferent(seq->tail);
  /* END SOLUTION */
}
/* END TEMPLATE */
