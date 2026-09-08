from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(max2Value(param[0])))

# BEGIN TEMPLATE
def max2Value(nums):
# BEGIN SOLUTION
  max=-10000000
  sec=-10000000
  for i in range(len(nums)):
    if nums[i] > max:
      sec = max
      max = nums[i]
    elif nums[i] > sec:
      sec = nums[i]
  return sec
# END SOLUTION
# END TEMPLATE
