#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int greenTicket(int a, int b, int c);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("i", greenTicket(params[0].as.i, params[1].as.i, params[2].as.i)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
int greenTicket(int a, int b, int c)
{
  /* BEGIN SOLUTION */
  if (a == b && b == c)
    return 20;
  else if (a == b || b == c || a == c)
    return 10;
  else // (a != b && b != a && c != a)
    return 0;
  /* END SOLUTION */
}
/* END TEMPLATE */
