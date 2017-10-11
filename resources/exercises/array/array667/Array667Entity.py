def array667(nums):
  count=0
  for i in range( len(nums)-1):
    if (nums[i] == 6) and (nums[i+1]==6 or nums[i+1]==7):
      count += 1
  return count
