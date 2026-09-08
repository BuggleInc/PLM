from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(parotTrouble(param[0], param[1])))

# BEGIN TEMPLATE
def parotTrouble(talking, hour):
# BEGIN SOLUTION
   return (talking and (hour<7 or hour>20))
# END SOLUTION
# END TEMPLATE
