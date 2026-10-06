#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int teaParty(int tea, int candy);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("i", teaParty(params[0].as.i, params[1].as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int teaParty(int tea, int candy)
{
  /* BEGIN SOLUTION */
  if (tea < 5 || candy < 5)
    return 0;
  else if (tea >= 2 * candy || candy >= 2 * tea)
    return 2;
  else // (tea >= 5 && candy >= 5)
    return 1;
  /* END SOLUTION */
}
/* END TEMPLATE */
