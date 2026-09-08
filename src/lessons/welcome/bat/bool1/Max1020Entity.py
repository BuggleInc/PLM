from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(max1020(param[0], param[1])))

# BEGIN TEMPLATE
def max1020(a, b):
# BEGIN SOLUTION
	A = max(a,b)
	B = min(a,b)
	if (A<21 and A>9):
		return A
	if (B<21 and B>9):
		return B
	return 0
# END SOLUTION
# END TEMPLATE
