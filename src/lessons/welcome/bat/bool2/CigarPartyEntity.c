#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool cigarParty(int cigars, bool isWeekend);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("b", cigarParty(params[0].as.i, params[1].as.b)));
    plm_value_free(params);
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
