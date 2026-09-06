def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(sleepIn(param[0], param[1])))

# BEGIN TEMPLATE
def sleepIn(weekday, vacation):
# BEGIN SOLUTION
    return not weekday or vacation
# END SOLUTION
# END TEMPLATE
