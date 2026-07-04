# BEGIN TEMPLATE
def butLast(list):
# BEGIN SOLUTION
  if list.tail == None:
    return None
  return cons(list.head,butLast(list.tail))
# END SOLUTION
# END TEMPLATE
