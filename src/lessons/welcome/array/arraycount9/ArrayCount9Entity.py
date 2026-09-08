from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(arrayCount9(param[0])))

# BEGIN TEMPLATE
def arrayCount9(nums):
# BEGIN SOLUTION
  res = 0
  for value in nums:
    if value == 9:
      res += 1
  return res
# END SOLUTION
# END TEMPLATE
