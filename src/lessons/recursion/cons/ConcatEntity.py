from RemoteCons import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(RecListToArray(concat(toRecListIfArray(param[0]), toRecListIfArray(param[1])))))

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
