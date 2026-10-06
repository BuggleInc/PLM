#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

RecList* concat(RecList* seq1, RecList* seq2);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    plm_value_t* result = recListToValue(concat(recListFromValue(args[0]), recListFromValue(args[1])));
    setTestResult(i, plm_serialize(result));
    plm_value_free(params);
    plm_value_free(result);
  }
}

/* BEGIN TEMPLATE */
RecList* concat(RecList* seq1, RecList* seq2)
{
  /* BEGIN SOLUTION */
  // Revert seq1 into A
  RecList* A = NULL;
  RecList* B = seq1;
  while (B != NULL) {
    A = cons(B->head, A);
    B = B->tail;
  }
  // add A at front of seq2 in B
  B = seq2;
  while (A != NULL) {
    B = cons(A->head, B);
    A = A->tail;
  }
  return B;
  /* END SOLUTION */
}
/* END TEMPLATE */
