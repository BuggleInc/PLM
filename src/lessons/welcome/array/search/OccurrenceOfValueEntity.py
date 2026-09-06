def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(occurrences(param[0], param[1])))

# BEGIN TEMPLATE
def occurrences(nums,lookingFor):
# BEGIN SOLUTION
  count = 0
  for i in range(len(nums)):
    if nums[i] == lookingFor:
      count += 1
  return count
# END SOLUTION
# END TEMPLATE
