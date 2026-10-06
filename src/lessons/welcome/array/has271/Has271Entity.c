#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool has271(int* nums, int len);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    int* nums           = (int*)plm_to_int_array(&params[0]);
    char* serialized    = plm_serialize_fmt("b", has271(nums, (int)params[0].as.array.size));
    free(nums);
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool has271(int* nums, int len)
{
  /* BEGIN SOLUTION */
  // Iterate < length-2, so can use i+1 and i+2 in the loop.
  // Return true immediately when seeing 271.
  for (int i = 0; i < (len - 2); i++) {
    int val = nums[i];
    if (nums[i + 1] == (val + 5) && abs(nums[i + 2] - (val - 1)) <= 2)
      return true;
  }

  // If we get here ... none found.
  return false;
  /* END SOLUTION */
}
/* END TEMPLATE */
