from RemoteCons import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(remove(toRecListIfArray(param[0]), toRecListIfArray(param[1]))))

# BEGIN TEMPLATE
def remove(list, v):
# BEGIN SOLUTION
  if list == None:
    return None;
  if list.head == v:
    return remove(list.tail, v)
  return cons(list.head, remove(list.tail, v))
# END SOLUTION
# END TEMPLATE
