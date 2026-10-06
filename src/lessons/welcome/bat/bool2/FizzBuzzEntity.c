#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

char* fizzBuzz(int a);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    char* serialized    = plm_serialize_fmt("s", fizzBuzz(params[0].as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
char* fizzBuzz(int a)
{
  /* BEGIN SOLUTION */
  char* res = malloc(16);
  if (a % 5 == 0 && a % 3 == 0)
    strcpy(res, "Fizz Buzz");
  else if (a % 5 == 0)
    strcpy(res, "Buzz");
  else if (a % 3 == 0)
    strcpy(res, "Fizz");
  else
    sprintf(res, "%d", a);
  return res;
  /* END SOLUTION */
}
/* END TEMPLATE */
