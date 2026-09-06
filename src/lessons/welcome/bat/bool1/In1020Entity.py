def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(in1020(param[0], param[1])))

# BEGIN TEMPLATE
def in1020(a, b):
# BEGIN SOLUTION
   return (a>9 and a<21) or (b>9 and b<21)
# END SOLUTION
# END TEMPLATE
