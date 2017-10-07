# BEGIN TEMPLATE
def nth(list, n):
# BEGIN SOLUTION
  if n == 1:
    return list.head
  return nth(list.tail, n-1)
# END SOLUTION
# END TEMPLATE
