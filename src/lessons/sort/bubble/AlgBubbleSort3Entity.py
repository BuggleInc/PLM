# BEGIN SOLUTION 
for i in range(getValueCount()-1,-1,-1):
  swapped = False
  for j in range(i):
    if not isSmaller(j,j+1):
      swap(j,j+1)
      swapped = True
  if not swapped:
    break
# END SOLUTION
