#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool squirrelPlay(int temp, bool isSummer);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("b", squirrelPlay(args[0]->as.i, args[1]->as.b));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool squirrelPlay(int temp, bool isSummer)
{
  /* BEGIN SOLUTION */
  return (temp >= 60 && ((isSummer && temp <= 100) || temp <= 90));
  /* END SOLUTION */
}
/* END TEMPLATE */
