def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(arrayFront9(param[0])))

# BEGIN TEMPLATE
def arrayFront9(nums):
# BEGIN SOLUTION
  for i in range( min( len(nums), 4) ):
    if nums[i] == 9:
      return True
  return False
# END SOLUTION
# END TEMPLATE
