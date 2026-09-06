def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(nfirst(toRecListIfArray(param[0]), toRecListIfArray(param[1]))))

# BEGIN TEMPLATE
def nfirst(list, n):
# BEGIN SOLUTION
  if n == 0:
    return None
  return cons(list.head, nfirst(list.tail, n-1))
# END SOLUTION
# END TEMPLATE
