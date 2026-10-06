#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool lastDigit(int a, int b, int c);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("b", lastDigit(params[0].as.i, params[1].as.i, params[2].as.i)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
bool lastDigit(int a, int b, int c)
{
  /* BEGIN SOLUTION */
  int da = a % 10;
  int db = b % 10;
  int dc = c % 10;
  return da == db || da == dc || dc == db;
  /* END SOLUTION */
}
/* END TEMPLATE */
