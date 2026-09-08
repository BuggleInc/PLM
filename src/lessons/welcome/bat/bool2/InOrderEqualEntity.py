from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(inOrderEqual(param[0], param[1], param[2], param[3])))

# BEGIN TEMPLATE
def inOrderEqual(a, b, c, equalOk):
# BEGIN SOLUTION
		return (equalOk and ((a <= b) and (b <= c))) or (a < b and b < c)
# END SOLUTION
# END TEMPLATE
