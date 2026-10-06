#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include "../../../../../target/classes/resources/langages/c/RemoteBat.h"

bool noTriples(int* nums, int len);
void run()
{
  int count = getTestCount();
  for (int i = 0; i < count; i++) {
    plm_value_t* params = plm_deserialize(getTest(i));
    plm_value_t** args  = params->as.array.elements;
    int* nums           = (int*)plm_to_int_array(args[0]);
    char* serialized    = plm_serialize_fmt("b", noTriples(nums, (int)args[0]->as.array.size));
    free(nums);
    plm_value_free(params);
    setTestResult(i, serialized);
    free(serialized);
  }
}

/* BEGIN TEMPLATE */
bool noTriples(int* nums, int len)
{
  /* BEGIN SOLUTION */
  // Iterate < length-2, so can use i+1 and i+2 in the loop.
  // Return false immediately if every seeing a triple.
  for (int i = 0; i < (len - 2); i++) {
    int first = nums[i];
    if (nums[i + 1] == first && nums[i + 2] == first)
      return false;
  }

  // If we get here ... no triples.
  return true;
  /* END SOLUTION */
}
/* END TEMPLATE */
