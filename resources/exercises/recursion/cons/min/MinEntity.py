# BEGIN TEMPLATE
def min(list):
# BEGIN SOLUTION
  ptr = list.tail
  v = list.head
  while ptr != None:
    if ptr.head < v:
      v = ptr.head
    ptr = ptr.tail
  return v
# END SOLUTION
# END TEMPLATE
