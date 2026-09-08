from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(sumDouble(param[0], param[1])))

# BEGIN TEMPLATE
def sumDouble(a, b):
# BEGIN SOLUTION
  if a==b:
    return (a+b)*2
  return a+b
# END SOLUTION
# END TEMPLATE
