#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
#include "universe/RecList.h"

int last(RecList* seq);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("i", last(recListFromValue(&params[0]))));
    plm_value_free(params);
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
