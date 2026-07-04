# BEGIN TEMPLATE
def butNlast(list, n):
# BEGIN SOLUTION
  if list == None or list.plmInsiderLength() <= n:
    return None
  return cons( list.head, butNlast(list.tail, n) )
# END SOLUTION
# END TEMPLATE
