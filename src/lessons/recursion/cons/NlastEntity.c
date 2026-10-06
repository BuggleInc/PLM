#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

RecList* nlast(RecList* seq, int n);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    plm_value_t* result = recListToValue(nlast(recListFromValue(args[0]), args[1]->as.i));
    setTestResult(i, plm_serialize(result));
    plm_value_free(params);
    free(test);
    plm_value_free(result);
  }
}

/* BEGIN TEMPLATE */
RecList* nlast(RecList* seq, int n)
{
  /* BEGIN SOLUTION */
  if (seq == NULL || recListLength(seq) <= n)
    return seq;
  return nlast(seq->tail, n);
  /* END SOLUTION */
}
/* END TEMPLATE */
