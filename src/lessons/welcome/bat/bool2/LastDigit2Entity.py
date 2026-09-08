from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(lastDigit(param[0], param[1], param[2])))

# BEGIN TEMPLATE
def lastDigit(a, b, c):
# BEGIN SOLUTION
	da = a % 10
	db = b % 10
	dc = c % 10
	return da == db or da == dc or dc == db
# END SOLUTION
# END TEMPLATE
