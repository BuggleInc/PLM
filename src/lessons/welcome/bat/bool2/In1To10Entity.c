#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool in1To10(int n, bool outsideMode);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("b", in1To10(params[0].as.i, params[1].as.b)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
bool in1To10(int n, bool outsideMode)
{
  /* BEGIN SOLUTION */
  return (outsideMode && (n <= 1 || n >= 10)) || ((!outsideMode) && (n >= 1 && n <= 10));
  /* END SOLUTION */
}
/* END TEMPLATE */
