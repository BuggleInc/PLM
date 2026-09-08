from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(stringSplosion(param[0])))

# BEGIN TEMPLATE
def stringSplosion(str):
# BEGIN SOLUTION
  res = ''
  for i in range(len(str)):
    res += str[0:i+1]
  return res
# END SOLUTION
# END TEMPLATE
