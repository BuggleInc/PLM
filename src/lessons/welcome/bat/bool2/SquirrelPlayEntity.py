from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(squirrelPlay(param[0], param[1])))

# BEGIN TEMPLATE
def squirrelPlay(temp, isSummer):
# BEGIN SOLUTION
   return (temp >= 60 and ((isSummer and temp <= 100) or temp <= 90))# END SOLUTION
# END TEMPLATE
