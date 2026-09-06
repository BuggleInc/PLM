def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(blueTicket(param[0], param[1], param[2])))

# BEGIN TEMPLATE
def blueTicket(a, b, c):
# BEGIN SOLUTION
	ab = a + b
	ac = a + c
	bc = b + c
	if (ab == 10 or ac == 10 or bc == 10):
		return 10
	elif (ab == (bc + 10) or ab == (ac + 10)):
		return 5
	else:
		return 0
# END SOLUTION
# END TEMPLATE
