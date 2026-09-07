def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(shareDigit(param[0], param[1])))

# BEGIN TEMPLATE
def shareDigit(a, b):
# BEGIN SOLUTION
   return (a//10 == b//10 or a//10 == b%10 or a%10 == b//10 or a%10 == b%10)
# END SOLUTION
# END TEMPLATE
