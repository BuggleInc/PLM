def indexOfValue(nums,lookingFor):
  for i in range(len(nums)):
    if nums[i]==lookingFor:
      return i
  return -1
