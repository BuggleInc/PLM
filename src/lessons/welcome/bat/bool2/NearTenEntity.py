from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(nearTen(param[0])))

# BEGIN TEMPLATE
def nearTen(num):
# BEGIN SOLUTION
  return (num % 10) <= 2 or (num % 10) >= 8
# END SOLUTION
# END TEMPLATE
