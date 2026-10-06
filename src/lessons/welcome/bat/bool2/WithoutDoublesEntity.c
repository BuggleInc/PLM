#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int withoutDoubles(int die1, int die2, bool noDoubles);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    setTestResult(i, plm_serialize_fmt("i", withoutDoubles(params[0].as.i, params[1].as.i, params[2].as.b)));
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
int withoutDoubles(int die1, int die2, bool noDoubles)
{
  /* BEGIN SOLUTION */
  if (noDoubles && (die1 == die2)) {
    if (die1 == 6)
      return 1 + die2;
    else
      return die1 + 1 + die2;
  } else
    return die1 + die2;
  /* END SOLUTION */
}
/* END TEMPLATE */
