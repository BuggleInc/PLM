# BEGIN TEMPLATE
def array123(nums):
  # BEGIN SOLUTION
  for i in range(len(nums)-2):
    if nums[i]==1  and  nums[i+1]==2  and  nums[i+2]==3:
      return True
  return False

# END SOLUTION
# END TEMPLATE
