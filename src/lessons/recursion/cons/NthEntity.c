#include "../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../target/classes/resources/langages/c/RemoteBat.h"
/* BEGIN IMPORT */
#include "universe/RecList.h"
/* END IMPORT */

int nth(RecList* seq, int n);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("i", nth(recListFromValue(&params[0]), params[1].as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int nth(RecList* seq, int n)
{
  /* BEGIN SOLUTION */
  if (n == 1)
    return seq->head;
  return nth(seq->tail, n - 1);
  /* END SOLUTION */
}
/* END TEMPLATE */
