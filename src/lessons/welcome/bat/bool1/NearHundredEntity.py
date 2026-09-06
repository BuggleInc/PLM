def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(nearHundred(param[0])))

# BEGIN TEMPLATE
def nearHundred(n):
# BEGIN SOLUTION
   return (90<=n and n<=110) or (190<=n and n<=210)
# END SOLUTION
# END TEMPLATE
