#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int indexOf(int* tab, int len, int lookingFor);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    int* tab            = (int*)plm_to_int_array(&params[0]);
    setTestResult(i, plm_serialize_fmt("i", indexOf(tab, (int)params[0].as.array.size, params[1].as.i)));
    free(tab);
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
// computes the index of the first value equals to 'lookingFor' contained in tab variable
int indexOf(int* tab, int len, int lookingFor)
{
  /* BEGIN SOLUTION */
  for (int i = 0; i < len; i++)
    if (tab[i] == lookingFor)
      return i;

  return -1;
  /* END SOLUTION */
}
/* END TEMPLATE */
