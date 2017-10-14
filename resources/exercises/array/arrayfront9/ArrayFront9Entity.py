# BEGIN TEMPLATE
def arrayFront9(nums):
  # BEGIN SOLUTION
  for i in range( min( len(nums), 4) ):
    if nums[i] == 9:
      return True
  return False

# END SOLUTION
# END TEMPLATE
