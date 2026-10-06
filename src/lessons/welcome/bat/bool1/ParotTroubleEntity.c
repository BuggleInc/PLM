#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool parotTrouble(bool talking, int hour);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("b", parotTrouble(args[0]->as.b, args[1]->as.i));
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool parotTrouble(bool talking, int hour)
{
  /* BEGIN SOLUTION */
  return (talking && (hour < 7 || hour > 20));
  /* END SOLUTION */
}
/* END TEMPLATE */
