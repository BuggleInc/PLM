# BEGIN TEMPLATE
def length(list):
# BEGIN SOLUTION
  if list == None:
    return 0
  return 1 + length(list.tail)
# END SOLUTION
# END TEMPLATE
