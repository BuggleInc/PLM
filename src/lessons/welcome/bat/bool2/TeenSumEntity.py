def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(teenSum(param[0], param[1])))

# BEGIN TEMPLATE
def teenSum(a, b):
# BEGIN SOLUTION
	if ((a >= 13 and a <= 19) or (b >= 13 and b <= 19)):
		return 19
	else:
		return a+b
# END SOLUTION
# END TEMPLATE
