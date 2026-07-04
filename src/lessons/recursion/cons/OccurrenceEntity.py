# BEGIN TEMPLATE
def occurences(list, val):
# BEGIN SOLUTION
  if list == None:
    return 0;
  if list.head == val:
    return 1 + occurences(list.tail, val)
  return occurences(list.tail, val)
# END SOLUTION
# END TEMPLATE
