def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(makes10(param[0], param[1])))

# BEGIN TEMPLATE
def makes10(a, b):
# BEGIN SOLUTION
   return a==10 or b==10 or (a+b)==10
# END SOLUTION
# END TEMPLATE
