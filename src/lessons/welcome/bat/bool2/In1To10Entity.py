from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(in1To10(param[0], param[1])))

# BEGIN TEMPLATE
def in1To10(n, outsideMode):
# BEGIN SOLUTION
   return (outsideMode and (n <= 1 or n >= 10)) or ((not outsideMode) and (n >= 1 and n <= 10))
# END SOLUTION
# END TEMPLATE
