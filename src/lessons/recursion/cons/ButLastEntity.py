def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(butLast(toRecListIfArray(param[0]))))

# BEGIN TEMPLATE
def butLast(list):
# BEGIN SOLUTION
  if list.tail == None:
    return None
  return cons(list.head, butLast(list.tail))
# END SOLUTION
# END TEMPLATE
