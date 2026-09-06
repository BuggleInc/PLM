def run():
    count = getTestCount()
    for i in range(count):
        param = deserialize(getTest(i))
        setTestResult(i, serialize(close10(param[0], param[1])))

# BEGIN TEMPLATE
import math
def close10(a, b):
# BEGIN SOLUTION
   if math.fabs(10-a) == math.fabs(10-b):
      return 0
   elif math.fabs(10-a) < math.fabs(10-b):
      return a
   else:
      return b
# END SOLUTION
# END TEMPLATE
