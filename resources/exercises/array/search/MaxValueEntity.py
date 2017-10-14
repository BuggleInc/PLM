# BEGIN TEMPLATE
def maxValue(nums):
  # BEGIN SOLUTION
  max=nums[0]
  for i in range(len(nums)):
    if nums[i] > max:
      max = nums[i]
  return max

# END SOLUTION
# END TEMPLATE
