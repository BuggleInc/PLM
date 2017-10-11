def max2Value(nums):
  max=-10000000
  sec=-10000000
  for i in range(len(nums)):
    if nums[i] > max:
      sec = max
      max = nums[i]
    elif nums[i] > sec:
      sec = nums[i]
  return sec
