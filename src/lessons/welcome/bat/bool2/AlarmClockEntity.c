#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

char* alarmClock(int day, bool vacation);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("s", alarmClock(params[0].as.i, params[1].as.b));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
char* alarmClock(int day, bool vacation)
{
  /* BEGIN SOLUTION */
  const char* res;
  if (!vacation) {
    if (day >= 1 && day <= 5)
      res = "7:00";
    else
      res = "10:00";
  } else {
    if (day >= 1 && day <= 5)
      res = "10:00";
    else
      res = "off";
  }
  char* copy = malloc(strlen(res) + 1);
  strcpy(copy, res);
  return copy;
  /* END SOLUTION */
}
/* END TEMPLATE */
