#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

char* alarmClock(int day, bool vacation);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* result        = alarmClock(args[0]->as.i, args[1]->as.b);
    char* serialized    = plm_serialize_fmt("s", result);
    free(result);
    plm_value_free(params);
    free(test);
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
