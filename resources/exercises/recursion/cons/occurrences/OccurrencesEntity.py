# BEGIN TEMPLATE
def occurrences(list, val):
  # BEGIN SOLUTION
  if list == None:
    return 0;
  if list.head == val:
    return 1 + occurrences(list.tail, val)
  return occurrences(list.tail, val)
  # END SOLUTION
# END TEMPLATE
