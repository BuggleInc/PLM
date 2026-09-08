from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(array123(param[0])))

# BEGIN TEMPLATE
def array123(nums):
# BEGIN SOLUTION
  for i in range(len(nums)-2):
    if nums[i]==1  and  nums[i+1]==2  and  nums[i+2]==3:
      return True
  return False
# END SOLUTION
# END TEMPLATE
