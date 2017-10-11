def inOrderEqual(a, b, c, equalOk):
  return (equalOk and ((a <= b) and (b <= c))) or (a < b and b < c)
