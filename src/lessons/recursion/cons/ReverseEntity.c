#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

RecList* reverse(RecList* seq);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    plm_value_t* result = recListToValue(reverse(recListFromValue(args[0])));
    char* serialized    = plm_serialize(result);
    plm_value_free(params);
    free(test);
    plm_value_free(result);
    recListFreeAll();
    setTestResult(i, serialized);
    free(serialized);
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
