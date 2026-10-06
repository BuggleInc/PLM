#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int redTicket(int a, int b, int c);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", redTicket(args[0]->as.i, args[1]->as.i, args[2]->as.i));
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int redTicket(int a, int b, int c)
{
  /* BEGIN SOLUTION */
  if (a == b && b == c && c == 2)
    return 10;
  else if (a == b && b == c)
    return 5;
  else if (b != a && c != a)
    return 1;
  else
    return 0;
  /* END SOLUTION */
}
/* END TEMPLATE */
