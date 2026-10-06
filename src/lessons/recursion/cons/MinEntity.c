#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
#include "universe/RecList.h"

int min(RecList* seq);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("i", min(recListFromValue(&params[0]))));
    plm_value_free(params);
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
