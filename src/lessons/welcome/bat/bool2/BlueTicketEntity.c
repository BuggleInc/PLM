#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int blueTicket(int a, int b, int c);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", blueTicket(args[0]->as.i, args[1]->as.i, args[2]->as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int blueTicket(int a, int b, int c)
{
  /* BEGIN SOLUTION */
  int ab = a + b;
  int ac = a + c;
  int bc = b + c;

  if (ab == 10 || ac == 10 || bc == 10)
    return 10;
  else if (ab == (bc + 10) || ab == (ac + 10))
    return 5;
  else
    return 0;
  /* END SOLUTION */
}
/* END TEMPLATE */
