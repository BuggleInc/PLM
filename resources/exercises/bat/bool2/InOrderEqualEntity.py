# BEGIN TEMPLATE
def inOrderEqual(a, b, c, equalOk):
  # BEGIN SOLUTION
  return (equalOk and ((a <= b) and (b <= c))) or (a < b and b < c)

# END SOLUTION
# END TEMPLATE
