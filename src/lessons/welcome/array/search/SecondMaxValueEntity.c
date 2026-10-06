#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"
#include <limits.h>

int max2Value(int* tab, int len);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    int* tab            = (int*)plm_to_int_array(&params[0]);
    char* serialized    = plm_serialize_fmt("i", max2Value(tab, (int)params[0].as.array.size));
    free(tab);
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
// computes the index of the second maximum of the values contained in tab variable
int max2Value(int* tab, int len)
{
  /* BEGIN SOLUTION */
  int max = INT_MIN;
  int sec = INT_MIN;
  for (int i = 0; i < len; i++)
    if (tab[i] > max) {
      sec = max;
      max = tab[i];
    } else if (tab[i] > sec) {
      sec = tab[i];
    }

  return sec;
  /* END SOLUTION */
}
/* END TEMPLATE */
