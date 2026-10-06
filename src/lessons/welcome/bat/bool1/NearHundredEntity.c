#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool nearHundred(int n);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("b", nearHundred(args[0]->as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool nearHundred(int n)
{
  /* BEGIN SOLUTION */
  return (90 <= n && n <= 110) || (190 <= n && n <= 210);
  /* END SOLUTION */
}
/* END TEMPLATE */
