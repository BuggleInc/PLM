from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(loneTeen(param[0], param[1])))

# BEGIN TEMPLATE
def loneTeen(a, b):
# BEGIN SOLUTION
	teenA = a>12 and a<20
	teenB = b>12 and b<20
	return  (teenA and not teenB) or (teenB and not teenA)
# END SOLUTION
# END TEMPLATE
