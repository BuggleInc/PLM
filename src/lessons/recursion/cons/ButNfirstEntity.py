from RemoteCons import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(RecListToArray(butNfirst(toRecListIfArray(param[0]), toRecListIfArray(param[1])))))

# BEGIN TEMPLATE
def butNfirst(list, n):
# BEGIN SOLUTION
  if list == None or n == 0:
    return list
  return butNfirst(list.tail, n-1)
# END SOLUTION
# END TEMPLATE
