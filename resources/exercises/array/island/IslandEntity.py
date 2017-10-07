# BEGIN TEMPLATE
def island(num):
# BEGIN SOLUTION
  nbisland=0
  for i in range(len(num)-1):
    if num[i]<num[i+1]:
      nbisland=nbisland+1
  return nbisland
# END SOLUTION
# END TEMPLATE
