from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(posNeg(param[0], param[1], param[2])))

# BEGIN TEMPLATE
def posNeg(a, b, negative):
# BEGIN SOLUTION
		if (negative):
			return a<0 and b<0;
		return (a<0 and b>0) or (a>0 and b<0)
# END SOLUTION
# END TEMPLATE
