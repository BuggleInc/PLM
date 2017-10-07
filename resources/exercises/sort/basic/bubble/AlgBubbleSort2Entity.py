# BEGIN SOLUTION 
for i in range(getValueCount()-1,-1,-1):
  for j in range(i):
    if not isSmaller(j,j+1):
      swap(j,j+1)
# END SOLUTION
