from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(stringTimes(param[0], param[1])))

# BEGIN TEMPLATE
def stringTimes(str, n):
# BEGIN SOLUTION
  res = ""
  for i in range(n):
    res += str
  return res
# END SOLUTION
# END TEMPLATE
