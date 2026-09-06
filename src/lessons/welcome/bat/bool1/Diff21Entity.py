def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(diff21(param[0])))

# BEGIN TEMPLATE
def diff21(n):
# BEGIN SOLUTION
   if (n>21):
      return 2*(n-21)
   return 21-n
# END SOLUTION
# END TEMPLATE
