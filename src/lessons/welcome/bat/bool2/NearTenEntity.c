#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool nearTen(int num);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("b", nearTen(params[0].as.i)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
bool nearTen(int num)
{
  /* BEGIN SOLUTION */
  return (num % 10) <= 2 || (num % 10) >= 8;
  /* END SOLUTION */
}
/* END TEMPLATE */
