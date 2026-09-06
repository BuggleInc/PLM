def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(reverse(toRecListIfArray(param[0]))))

# BEGIN TEMPLATE
def reverse(list):
# BEGIN SOLUTION
  A = None
  B = list
  while B != None:
     A = cons (B.head, A)
     B = B.tail
  return A
# END SOLUTION
# END TEMPLATE
