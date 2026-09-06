def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(twoAsOne(param[0], param[1], param[2])))

# BEGIN TEMPLATE
def twoAsOne(a, b, c):
# BEGIN SOLUTION
   return (a + b == c) or (a + c == b) or (b + c == a)
# END SOLUTION
# END TEMPLATE
