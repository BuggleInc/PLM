# BEGIN TEMPLATE
def concat(list1, list2):
  # BEGIN SOLUTION
  # Revert l1 into A
  A = None
  B = list1
  while B != None:
    A = RecList(B.head, A)
    B = B.tail
  # add A at front of l2 in B
  B = list2
  while A != None:
    B = RecList(A.head, B)
    A = A.tail
  return B
  # END SOLUTION
# END TEMPLATE
