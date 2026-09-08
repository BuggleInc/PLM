from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(monkeyTrouble(param[0], param[1])))

# BEGIN TEMPLATE
def monkeyTrouble(aSmile, bSmile):
# BEGIN SOLUTION
   return (aSmile and bSmile) or (not aSmile and not bSmile)
# END SOLUTION
# END TEMPLATE
