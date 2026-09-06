def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(butNlast(toRecListIfArray(param[0]), toRecListIfArray(param[1]))))

# BEGIN TEMPLATE
def butNlast(list, n):
# BEGIN SOLUTION
  if list == None or list.plmInsiderLength() <= n:
    return None
  return cons( list.head, butNlast(list.tail, n) )
# END SOLUTION
# END TEMPLATE
