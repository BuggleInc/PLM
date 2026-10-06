#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

int occurences(RecList* seq, int val);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", occurences(recListFromValue(args[0]), args[1]->as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int occurences(RecList* seq, int val)
{
  /* BEGIN SOLUTION */
  if (seq == NULL)
    return 0;
  if (seq->head == val)
    return 1 + occurences(seq->tail, val);
  return occurences(seq->tail, val);
  /* END SOLUTION */
}
/* END TEMPLATE */
