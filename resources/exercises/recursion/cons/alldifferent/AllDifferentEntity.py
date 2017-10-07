# BEGIN TEMPLATE
def allDifferent(list):
# BEGIN SOLUTION
  if list == None:
    return True
  ptr = list.tail
  while ptr != None and ptr.head != list.head:
    ptr = ptr.tail
  if ptr != None:
    return False
  return allDifferent(list.tail)
# END SOLUTION
# END TEMPLATE
