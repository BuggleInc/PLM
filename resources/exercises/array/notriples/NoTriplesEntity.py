# BEGIN TEMPLATE
def noTriples(nums):
  # BEGIN SOLUTION
  count=0
  for i in range( len(nums)-2 ):
    if (nums[i] == nums[i+1]) and (nums[i+1] == nums[i+2]):
      return False
  return True

# END SOLUTION
# END TEMPLATE
