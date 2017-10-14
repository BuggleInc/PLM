import math
# BEGIN TEMPLATE
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
