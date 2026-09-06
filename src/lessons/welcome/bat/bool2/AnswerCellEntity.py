def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(answerCell(param[0], param[1], param[2])))

# BEGIN TEMPLATE
def answerCell(isMorning, isMom, isAsleep):
# BEGIN SOLUTION
   return (not isAsleep) and not (isMorning and not isMom)
# END SOLUTION
# END TEMPLATE
