from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(alarmClock(param[0], param[1])))

# BEGIN TEMPLATE
def alarmClock(day, vacation):
# BEGIN SOLUTION
	if not vacation:
		if (day >= 1 and day <= 5):
			return '7:00'
		else:
			return '10:00'
	else:
		if (day >= 1 and day <= 5):
			return '10:00'
		else:
			return 'off'
# END SOLUTION
# END TEMPLATE
