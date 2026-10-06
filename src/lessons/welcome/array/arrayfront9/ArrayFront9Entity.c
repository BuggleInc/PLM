#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool arrayFront9(int* nums, int len);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    int* nums           = (int*)plm_to_int_array(&params[0]);
    setTestResult(i, plm_serialize_fmt("b", arrayFront9(nums, (int)params[0].as.array.size)));
    free(nums);
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
bool arrayFront9(int* nums, int len)
{
  /* BEGIN SOLUTION */
  // First figure the end for the loop
  int end = len;
  if (end > 4)
    end = 4;

  for (int i = 0; i < end; i++) {
    if (nums[i] == 9)
      return true;
  }

  return false;
  /* END SOLUTION */
}
/* END TEMPLATE */
