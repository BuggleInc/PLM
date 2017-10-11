def averageValue(nums):
  total = 0
  for i in range(len(nums)):
    total += nums[i]
  return total / len(nums)
