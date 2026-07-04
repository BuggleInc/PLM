# BEGIN TEMPLATE
def remove(list, v):
# BEGIN SOLUTION
  if list == None:
    return None;
  if list.head == v:
    return remove(list.tail, v)
  return cons(list.head, remove(list.tail, v))
# END SOLUTION
# END TEMPLATE
