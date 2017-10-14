# BEGIN TEMPLATE
def indexOfValue(nums,lookingFor):
  # BEGIN SOLUTION
  for i in range(len(nums)):
    if nums[i]==lookingFor:
      return i
  return -1

# END SOLUTION
# END TEMPLATE
