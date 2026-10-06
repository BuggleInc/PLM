#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

int arrayCount9(int* nums, int len);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    char* test          = getTest(i);
    plm_value_t* params = plm_deserialize(test);
    plm_value_t** args  = params->as.array.elements;
    int* nums           = (int*)plm_to_int_array(args[0]);
    char* serialized    = plm_serialize_fmt("i", arrayCount9(nums, (int)args[0]->as.array.size));
    free(nums);
    plm_value_free(params);
    free(test);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
int arrayCount9(int* nums, int len)
{
  /* BEGIN SOLUTION */
  int count = 0;
  for (int i = 0; i < len; i++) {
    if (nums[i] == 9) {
      count++;
    }
  }
  return count;
  /* END SOLUTION */
}
/* END TEMPLATE */
