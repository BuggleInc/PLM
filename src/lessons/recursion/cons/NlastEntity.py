# BEGIN TEMPLATE
def nlast(list, n):
# BEGIN SOLUTION
  if list == None or list.plmInsiderLength() <= n:
    return list
  return nlast(list.tail, n)
# END SOLUTION
# END TEMPLATE
