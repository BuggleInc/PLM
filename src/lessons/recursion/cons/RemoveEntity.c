#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
#include "universe/RecList.h"

RecList* remove_(RecList* seq, int v);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t* result = recListToValue(remove_(recListFromValue(&params[0]), params[1].as.i));
    setTestResult(i, plm_serialize(result));
    plm_value_free(params);
    plm_value_free(result);
  }
}

/* BEGIN TEMPLATE */
RecList* remove_(RecList* seq, int v)
{
  /* BEGIN SOLUTION */
  if (seq == NULL)
    return NULL;
  if (seq->head == v)
    return remove_(seq->tail, v);
  return cons(seq->head, remove_(seq->tail, v));
  /* END SOLUTION */
}
/* END TEMPLATE */
