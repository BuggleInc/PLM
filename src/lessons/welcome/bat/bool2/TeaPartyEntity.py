def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(teaParty(param[0], param[1])))

# BEGIN TEMPLATE
def teaParty(tea, candy):
# BEGIN SOLUTION
	if (tea < 5 or candy < 5):
		return 0
	elif (tea >= 2*candy or candy >= 2*tea):
		return 2
	else:
		return 1
# END SOLUTION
# END TEMPLATE
