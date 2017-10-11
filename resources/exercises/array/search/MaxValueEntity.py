def maxValue(nums):
  max=nums[0]
  for i in range(len(nums)):
    if nums[i] > max:
      max = nums[i]
  return max
