def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(indexOfMaxValue(param[0])))

# BEGIN TEMPLATE
def indexOfMaxValue(nums):
# BEGIN SOLUTION
  max=nums[0]
  maxIdx = 0
  for i in range(len(nums)):
    if nums[i]>max:
      max = nums[i]
      maxIdx = i
  return maxIdx
# END SOLUTION
# END TEMPLATE
