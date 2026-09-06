def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(caughtSpeeding(param[0], param[1])))

# BEGIN TEMPLATE
def caughtSpeeding(speed, isBirthday):
# BEGIN SOLUTION
	if ((isBirthday and speed <= 65) or (speed <= 60)):
		return 0
	elif ((isBirthday and speed <= 85) or (speed <= 80)):
		return 1
	else:
		return 2
# END SOLUTION
# END TEMPLATE
