#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

int length(RecList* seq);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("i", length(recListFromValue(&params[0]))));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
int length(RecList* seq)
{
  /* BEGIN SOLUTION */
  if (seq == NULL)
    return 0;
  return 1 + length(seq->tail);
  /* END SOLUTION */
}
/* END TEMPLATE */
