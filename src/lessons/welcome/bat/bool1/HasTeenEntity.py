from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(hasTeen(param[0], param[1], param[2])))

# BEGIN TEMPLATE
def hasTeen(a, b, c):
# BEGIN SOLUTION
   return (a>12 and a<20) or (b>12 and b<20) or (c>12 and c<20)
# END SOLUTION
# END TEMPLATE
