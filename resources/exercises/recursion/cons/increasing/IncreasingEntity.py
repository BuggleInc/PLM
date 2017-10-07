# BEGIN TEMPLATE
def increasing(list):
# BEGIN SOLUTION
  if list == None or list.tail == None:
    return True
  if list.head > list.tail.head:
    return False
  return increasing(list.tail)
# END SOLUTION
# END TEMPLATE
