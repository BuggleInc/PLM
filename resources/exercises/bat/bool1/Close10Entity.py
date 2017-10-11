import math
def close10(a, b):
   if math.fabs(10-a) == math.fabs(10-b):
      return 0
   elif math.fabs(10-a) < math.fabs(10-b):
      return a
   else:
      return b
