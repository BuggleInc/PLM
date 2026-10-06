#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int golomb(int num);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", golomb(args[0]->as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int golomb(int num)
{
  /* BEGIN SOLUTION */
  if (num == 1) {
    return 1;
  } else {
    return 1 + golomb(num - golomb(golomb(num - 1)));
  }
  /* END SOLUTION */
}
/* END TEMPLATE */
