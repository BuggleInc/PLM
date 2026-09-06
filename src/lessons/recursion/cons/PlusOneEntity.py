def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(plusOne(toRecListIfArray(param[0]))))

# BEGIN TEMPLATE
def plusOne(list):
# BEGIN SOLUTION
  if list == None:
    return None
  return cons(list.head+1, plusOne(list.tail))
# END SOLUTION
# END TEMPLATE
