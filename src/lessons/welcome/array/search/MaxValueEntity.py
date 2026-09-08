from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(maxValue(param[0])))

# BEGIN TEMPLATE
def maxValue(nums):
# BEGIN SOLUTION
  max=nums[0]
  for i in range(len(nums)):
    if nums[i] > max:
      max = nums[i]
  return max
# END SOLUTION
# END TEMPLATE
