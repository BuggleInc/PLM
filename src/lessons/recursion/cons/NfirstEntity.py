# BEGIN TEMPLATE
def nfirst(list, n):
# BEGIN SOLUTION
  if n == 0:
    return None
  return cons(list.head, nfirst(list.tail, n-1))
# END SOLUTION
# END TEMPLATE
