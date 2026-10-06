#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

RecList* plusOne(RecList* seq);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t* result = recListToValue(plusOne(recListFromValue(&params[0])));
    setTestResult(i, plm_serialize(result));
    plm_value_free(params);
    plm_value_free(result);
  }
}

/* BEGIN TEMPLATE */
RecList* plusOne(RecList* seq)
{
  /* BEGIN SOLUTION */
  if (seq == NULL)
    return NULL;
  return cons(seq->head + 1, plusOne(seq->tail));
  /* END SOLUTION */
}
/* END TEMPLATE */
