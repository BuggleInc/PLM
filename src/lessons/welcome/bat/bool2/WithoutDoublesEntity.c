#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int withoutDoubles(int die1, int die2, bool noDoubles);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", withoutDoubles(args[0]->as.i, args[1]->as.i, args[2]->as.b));
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
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
