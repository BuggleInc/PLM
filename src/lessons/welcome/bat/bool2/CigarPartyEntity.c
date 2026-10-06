#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool cigarParty(int cigars, bool isWeekend);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("b", cigarParty(args[0]->as.i, args[1]->as.b));
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool cigarParty(int cigars, bool isWeekend)
{
  /* BEGIN SOLUTION */
  return (isWeekend && cigars >= 40) || (!isWeekend && (cigars >= 40) && (cigars <= 60));
  /* END SOLUTION */
}
/* END TEMPLATE */
