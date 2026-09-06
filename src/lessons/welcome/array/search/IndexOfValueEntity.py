def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(indexOfValue(param[0], param[1])))

# BEGIN TEMPLATE
def indexOfValue(nums,lookingFor):
# BEGIN SOLUTION
  for i in range(len(nums)):
    if nums[i]==lookingFor:
      return i
  return -1
# END SOLUTION
# END TEMPLATE
