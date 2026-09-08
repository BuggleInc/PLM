from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(altPairs(param[0])))

# BEGIN TEMPLATE
def altPairs(str):
# BEGIN SOLUTION
  res = ''
  for i in range(0, len(str), 4):
    res += str[i:i+2]
  return res
# END SOLUTION
# END TEMPLATE