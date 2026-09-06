def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(sortaSum(param[0], param[1])))

# BEGIN TEMPLATE
def sortaSum(a, b):
# BEGIN SOLUTION
	sum = a+b
	if (sum >= 10 and sum <= 19):
		return 20
	else:
		return sum
# END SOLUTION
# END TEMPLATE
