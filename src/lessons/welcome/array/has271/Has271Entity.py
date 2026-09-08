from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(has271(param[0])))

# BEGIN TEMPLATE
import math
def has271(nums):
# BEGIN SOLUTION
  count=0
  for i in range( len(nums)-1):
    if (nums[i] + 5 == nums[i+1]) and (math.fabs(nums[i+2]-nums[i]+1)<=2):
      return True
  return False
# END SOLUTION
# END TEMPLATE
