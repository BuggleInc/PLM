# BEGIN TEMPLATE
def posNeg(a, b, negative):
# BEGIN SOLUTION
  if (negative):
    return a<0 and b<0
  return (a<0 and b>0) or (a>0 and b<0)
# END SOLUTION
# END TEMPLATE
