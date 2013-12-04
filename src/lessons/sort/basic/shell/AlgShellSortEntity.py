# BEGIN SOLUTION 
gap = getValueCount()/2

# while h remains larger than 0
while gap>0:
  # for each set of elements (there are h sets)
  for i in range(gap-1,getValueCount()):
    # pick the last element in the set
    value = getValue(i)
    j = i
    # compare the element at B to the one before it in the set
    # if they are out of order continue this loop, moving
    # elements "back" to make room for B to be inserted.
    while (j >= gap) and (getValue(j-gap) > value):
      copy(j-gap,j)
      j = j - gap
    # insert B into the correct place
    setValue(j, value)
  # all sets gap-sorted, now decrease set size
  gap = gap / 2
# END SOLUTION
