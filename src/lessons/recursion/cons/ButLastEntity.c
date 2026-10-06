#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

RecList* butLast(RecList* seq);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    plm_value_t* result = recListToValue(butLast(recListFromValue(args[0])));
    setTestResult(i, plm_serialize(result));
    plm_value_free(params);
    free(test);
    plm_value_free(result);
  }
}

/* BEGIN TEMPLATE */
RecList* butLast(RecList* seq)
{
  /* BEGIN SOLUTION */
  if (seq->tail == NULL)
    return NULL;
  return cons(seq->head, butLast(seq->tail));
  /* END SOLUTION */
}
/* END TEMPLATE */
