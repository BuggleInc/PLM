#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

int min(RecList* seq);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", min(recListFromValue(args[0])));
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int min(RecList* seq)
{
  /* BEGIN SOLUTION */
  int v        = seq->head;
  RecList* ptr = seq;
  while (ptr != NULL) {
    if (ptr->head < v)
      v = ptr->head;
    ptr = ptr->tail;
  }
  return v;
  /* END SOLUTION */
}
/* END TEMPLATE */
