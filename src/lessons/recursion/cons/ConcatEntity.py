# BEGIN TEMPLATE
def concat(list1, list2):
# BEGIN SOLUTION
  A = None
  B = list1
  while B != None:
     A = cons (B.head, A)
     B = B.tail
  B = list2
  while A != None:
     B = cons(A.head, B)
     A = A.tail
  return B# END SOLUTION
# END TEMPLATE
