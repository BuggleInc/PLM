#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int countTeen(int a, int b, int c, int d);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", countTeen(args[0]->as.i, args[1]->as.i, args[2]->as.i, args[3]->as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int countTeen(int a, int b, int c, int d)
{
  /* BEGIN SOLUTION */
  int ret = 0;
  if (a > 12 && a < 20)
    ret += 1;
  if (b > 12 && b < 20)
    ret += 1;
  if (c > 12 && c < 20)
    ret += 1;
  if (d > 12 && d < 20)
    ret += 1;
  return ret;
  /* END SOLUTION */
}
/* END TEMPLATE */
