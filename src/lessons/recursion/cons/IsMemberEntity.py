from RemoteCons import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(isMember(toRecListIfArray(param[0]), toRecListIfArray(param[1]))))

# BEGIN TEMPLATE
def isMember(list, val):
# BEGIN SOLUTION
  if list == None:
    return False
  if list.head == val:
    return True
  return isMember(list.tail, val)
# END SOLUTION
# END TEMPLATE
