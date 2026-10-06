#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int array667(int* nums, int len);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    int* nums           = (int*)plm_to_int_array(&params[0]);
    setTestResult(i, plm_serialize_fmt("i", array667(nums, (int)params[0].as.array.size)));
    free(nums);
    plm_value_free(params);
  }
}

/* BEGIN TEMPLATE */
int array667(int* nums, int len)
{
  /* BEGIN SOLUTION */
  int count = 0;
  // Note: iterate to length-1, so can use i+1 in the loop
  for (int i = 0; i < (len - 1); i++) {
    if (nums[i] == 6) {
      if (nums[i + 1] == 6 || nums[i + 1] == 7) {
        count++;
      }
    }
  }
  return count;
  /* END SOLUTION */
}
/* END TEMPLATE */
