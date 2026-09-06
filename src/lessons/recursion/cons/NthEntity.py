def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(nth(toRecListIfArray(param[0]), toRecListIfArray(param[1]))))

# BEGIN TEMPLATE
def nth(list, n):
# BEGIN SOLUTION
  if n == 1:
    return list.head
  return nth(list.tail, n-1)
# END SOLUTION
# END TEMPLATE
