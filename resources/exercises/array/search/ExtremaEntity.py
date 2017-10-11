def extrema(nums):
  if (len(nums)>0) :
    min=nums[0]
    max=nums[0]
    for i in range( len(nums)-1):
      if (nums[i+1]<min) :
      	 min=nums[i+1]
      if (nums[i+1]>max) :
      	 max=nums[i+1]
    return max-min
  else :
    return 0
