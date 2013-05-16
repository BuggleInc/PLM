# BEGIN TEMPLATE
def dragon(ordre, x, y, z, t):
  # BEGIN SOLUTION 
    if ordre == 1):
      setPos(x, y)
      moveTo(z, t)
    else:
      u = (x + z + t - y) / 2
      v = (y + t - z + x) / 2;
      dragon(ordre - 1, x, y, u, v)
      dragon(ordre - 1, z, t, u, v)
# END SOLUTION
# END TEMPLATE

dragon(getParam(0), getParam(1), getParam(2), getParam(3), getParam(4))
