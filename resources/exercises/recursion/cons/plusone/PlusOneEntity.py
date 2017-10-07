# BEGIN TEMPLATE
def plusOne(list):
  # BEGIN SOLUTION
  if list == None:
    return None
  return RecList(list.head+1, plusOne(list.tail))
  # END SOLUTION
# END TEMPLATE
