from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(lastDigit(param[0], param[1])))

# BEGIN TEMPLATE
def lastDigit(a, b):
# BEGIN SOLUTION
   return a%10 == b%10
# END SOLUTION
# END TEMPLATE
