def occurrences(nums,lookingFor):
  count = 0
  for i in range(len(nums)):
    if nums[i] == lookingFor:
      count += 1
  return count
