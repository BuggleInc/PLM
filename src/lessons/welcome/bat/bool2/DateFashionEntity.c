#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int dateFashion(int you, int date);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("i", dateFashion(args[0]->as.i, args[1]->as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int dateFashion(int you, int date)
{
  /* BEGIN SOLUTION */
  if (you <= 2 || date <= 2)
    return 0;
  else if (you >= 8 || date >= 8)
    return 2;
  else
    return 1;
  /* END SOLUTION */
}
/* END TEMPLATE */
