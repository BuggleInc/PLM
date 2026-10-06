#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int extrema(int* nums, int len);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    int* nums           = (int*)plm_to_int_array(&params[0]);
    char* serialized    = plm_serialize_fmt("i", extrema(nums, (int)params[0].as.array.size));
    free(nums);
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int extrema(int* nums, int len)
{
  /* BEGIN SOLUTION */
  if (len > 0) {
    int min = nums[0];
    int max = nums[0];
    for (int i = 1; i < len; i++) {
      if (nums[i] < min) {
        min = nums[i];
      }
      if (nums[i] > max) {
        max = nums[i];
      }
    }
    return max - min;
  } else {
    return 0;
  }
  /* END SOLUTION */
}
/* END TEMPLATE */
