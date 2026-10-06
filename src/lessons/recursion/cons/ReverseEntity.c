#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
#include "universe/RecList.h"

RecList* reverse(RecList* seq);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t* result = recListToValue(reverse(recListFromValue(&params[0])));
    setTestResult(i, plm_serialize(result));
    plm_value_free(params);
    plm_value_free(result);
  }
}

/* BEGIN TEMPLATE */
RecList* reverse(RecList* seq)
{
  /* BEGIN SOLUTION */
  RecList* A = NULL;
  RecList* B = seq;
  while (B != NULL) {
    A = cons(B->head, A);
    B = B->tail;
  }
  return A;
  /* END SOLUTION */
}
/* END TEMPLATE */
