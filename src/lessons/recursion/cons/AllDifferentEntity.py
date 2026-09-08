from RemoteCons import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(allDifferent(toRecListIfArray(param[0]))))

# BEGIN TEMPLATE
def allDifferent(list):
# BEGIN SOLUTION
  if list == None:
    return True;
  ptr = list.tail
  while ptr != None and ptr.head != list.head:
    ptr = ptr.tail
  if ptr != None:
    return False
  return allDifferent(list.tail)
# END SOLUTION
# END TEMPLATE
