def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(averageValue(param[0])))

# BEGIN TEMPLATE
def averageValue(nums):
# BEGIN SOLUTION
  total = 0
  for i in range(len(nums)):
    total += nums[i]
  return total / len(nums)
# END SOLUTION
# END TEMPLATE
