# BEGIN TEMPLATE
def butLast(list):
  print list
  # BEGIN SOLUTION
  if list.tail == None:
    return None
  return RecList(list.head,butLast(list.tail))
  # END SOLUTION
# END TEMPLATE
