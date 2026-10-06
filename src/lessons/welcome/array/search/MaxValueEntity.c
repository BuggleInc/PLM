#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int maxValue(int* tab, int len);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    int* tab            = (int*)plm_to_int_array(args[0]);
    char* serialized    = plm_serialize_fmt("i", maxValue(tab, (int)args[0]->as.array.size));
    free(tab);
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
// computes the index of the maximum of the values contained in tab variable
int maxValue(int* tab, int len)
{
  /* BEGIN SOLUTION */
  int max = tab[0];
  for (int i = 1; i < len; i++)
    if (tab[i] >= max)
      max = tab[i];

  return max;
  /* END SOLUTION */
}
/* END TEMPLATE */
