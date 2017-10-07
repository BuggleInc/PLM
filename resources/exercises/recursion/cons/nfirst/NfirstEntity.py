# BEGIN TEMPLATE
def nfirst(list, n):
  # BEGIN SOLUTION
  if n == 0:
    return None
  return RecList(list.head, nfirst(list.tail, n-1))
  # END SOLUTION
# END TEMPLATE
