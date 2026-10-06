#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int island(int* num, int len);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    int* num            = (int*)plm_to_int_array(&params[0]);
    char* serialized    = plm_serialize_fmt("i", island(num, (int)params[0].as.array.size));
    free(num);
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int island(int* num, int len)
{
  /* BEGIN SOLUTION */
  int nbisland = 0;
  for (int i = 0; i < len - 1; i++) {
    if (num[i] < num[i + 1]) {
      nbisland++;
    }
  }
  return nbisland;
  /* END SOLUTION */
}
/* END TEMPLATE */
