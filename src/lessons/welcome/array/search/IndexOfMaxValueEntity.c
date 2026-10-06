#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"
#include <limits.h>

int indexOfMaximum(int* tab, int len);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    int* tab            = (int*)plm_to_int_array(&params[0]);
    setTestResult(i, plm_serialize_fmt("i", indexOfMaximum(tab, (int)params[0].as.array.size)));
    free(tab);
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
// computes the index of the maximum of the values contained in tab variable
int indexOfMaximum(int* tab, int len)
{
  /* BEGIN SOLUTION */
  int max   = INT_MIN;
  int index = 0;
  for (int i = 0; i < len; i++) {
    if (tab[i] > max) { // we are looking for the first occurence
      max   = tab[i];
      index = i;
    }
  }
  return index;
  /* END SOLUTION */
}
/* END TEMPLATE */
