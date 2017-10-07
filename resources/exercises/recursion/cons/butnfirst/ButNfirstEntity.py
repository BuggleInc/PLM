# BEGIN TEMPLATE
def butNfirst(list, n):
  # BEGIN SOLUTION
  if list == None or n == 0:
    return list
  return butNfirst(list.tail, n-1)
  # END SOLUTION
# END TEMPLATE
