# BEGIN TEMPLATE
def isMember(list, val):
# BEGIN SOLUTION
  if list == None:
    return False;
  if list.head == val:
    return True
  return isMember(list.tail, val)
# END SOLUTION
# END TEMPLATE
