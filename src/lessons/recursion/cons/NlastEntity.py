from RemoteCons import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(nlast(toRecListIfArray(param[0]), toRecListIfArray(param[1]))))

# BEGIN TEMPLATE
def nlast(list, n):
# BEGIN SOLUTION
  if list == None or list.plmInsiderLength() <= n:
    return list
  return nlast(list.tail, n)
# END SOLUTION
# END TEMPLATE
