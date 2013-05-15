# BEGIN SOLUTION
for i in range(getValueCount()):
  value = getValue(i)
  j = i
  while j>0 and not isSmallerThan(j-1,value):
    copy(j-1,j)
    j = j-1
  setValue(j,value)
# END TEMPLATE
checkme()

