def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(inOrder(param[0], param[1], param[2], param[3])))

# BEGIN TEMPLATE
def inOrder(a, b, c, bOk):
# BEGIN SOLUTION
		return (bOk or (b > a)) and (c > b)
# END SOLUTION
# END TEMPLATE
