def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(last(toRecListIfArray(param[0]))))

# BEGIN TEMPLATE
def last(list):
# BEGIN SOLUTION
  if list.tail == None:
    return list.head
  return last(list.tail)
# END SOLUTION
# END TEMPLATE
