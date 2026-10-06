#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

int last(RecList* seq);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", last(recListFromValue(args[0])));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int last(RecList* seq)
{
  /* BEGIN SOLUTION */
  if (seq->tail == NULL)
    return seq->head;
  return last(seq->tail);
  /* END SOLUTION */
}
/* END TEMPLATE */
