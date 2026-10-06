#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool loneTeen(int a, int b);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    char* serialized    = plm_serialize_fmt("b", loneTeen(args[0]->as.i, args[1]->as.i));
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool loneTeen(int a, int b)
{
  /* BEGIN SOLUTION */
  bool teenA = a > 12 && a < 20;
  bool teenB = b > 12 && b < 20;
  return (teenA && !teenB) || (teenB && !teenA);
  /* END SOLUTION */
}
/* END TEMPLATE */
