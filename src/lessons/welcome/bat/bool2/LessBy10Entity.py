def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(lessBy10(param[0], param[1], param[2])))

# BEGIN TEMPLATE
def lessBy10(a, b, c):
# BEGIN SOLUTION
	return ((a - b) >= 10) or ((b - a) >= 10) or ((b - c) >= 10) or ((c - b) >= 10) or ((a - c) >= 10) or ((c - a) >= 10)
# END SOLUTION
# END TEMPLATE
