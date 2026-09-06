def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(extrema(param[0])))

# BEGIN TEMPLATE
def extrema(nums):
# BEGIN SOLUTION
  if (len(nums)>0) :
    min=nums[0]
    max=nums[0]
    for i in range( len(nums)-1):
      if (nums[i+1]<min) :
      	 min=nums[i+1]
      if (nums[i+1]>max) :
      	 max=nums[i+1]
    return max-min
  else :
    return 0
# END SOLUTION
# END TEMPLATE
