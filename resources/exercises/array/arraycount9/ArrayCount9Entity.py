def arrayCount9(nums):
  res = 0
  for value in nums:
    if value == 9:
      res += 1
  return res
