from RemoteBat import *
def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(withoutDoubles(param[0], param[1], param[2])))

# BEGIN TEMPLATE
def withoutDoubles(die1, die2, noDoubles):
# BEGIN SOLUTION
	if (noDoubles and (die1 == die2)):
		if (die1 == 6):
			return 1 + die2
		else:
			return die1 + 1 + die2
	else:
		return die1 + die2
# END SOLUTION
# END TEMPLATE
