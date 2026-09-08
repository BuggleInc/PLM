from RemoteCons import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(min(toRecListIfArray(param[0]))))

# BEGIN TEMPLATE
def min(list):
# BEGIN SOLUTION
  ptr = list.tail
  v = list.head
  while ptr != None:
     if ptr.head < v:
        v = ptr.head
     ptr = ptr.tail
  return v
# END SOLUTION
# END TEMPLATE
