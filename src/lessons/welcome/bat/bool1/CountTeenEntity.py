from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(countTeen(param[0], param[1], param[2], param[3])))

# BEGIN TEMPLATE
def countTeen(a, b, c, d):
# BEGIN SOLUTION
		ret=0
		if (a>12 and a<20):
			ret+=1
		if (b>12 and b<20):
			ret+=1
		if (c>12 and c<20):
			ret+=1
		if (d>12 and d<20):
			ret+=1
		return ret
# END SOLUTION
# END TEMPLATE
