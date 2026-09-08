from RemoteCons import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(length(toRecListIfArray(param[0]))))

# BEGIN TEMPLATE
def length(list):
# BEGIN SOLUTION
  if list == None:
    return 0
  return 1 + length(list.tail)
# END SOLUTION
# END TEMPLATE
