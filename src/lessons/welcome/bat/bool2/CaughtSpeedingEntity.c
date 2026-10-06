#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int caughtSpeeding(int speed, bool isBirthday);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("i", caughtSpeeding(params[0].as.i, params[1].as.b));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int caughtSpeeding(int speed, bool isBirthday)
{
  /* BEGIN SOLUTION */
  if ((isBirthday && speed <= 65) || (speed <= 60))
    return 0;
  else if ((isBirthday && speed <= 85) || (speed <= 80))
    return 1;
  else
    return 2;
  /* END SOLUTION */
}
/* END TEMPLATE */
