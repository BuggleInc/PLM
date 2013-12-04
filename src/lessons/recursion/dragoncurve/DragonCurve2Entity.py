# BEGIN TEMPLATE
def dragon(order, x, y, z, t):
  # BEGIN HIDDEN
  if (order == 1):
    setColor(Color.red)
    moveTo(z, t)
  else:
    u = (x + z + t - y) / 2
    v = (y + t - z + x) / 2
    dragon(order - 1, x, y, u, v)
    dragonInverse(order - 1, u, v, z, t)
  # END HIDDEN

def dragonInverse(order, x, y, z, t):
  # BEGIN HIDDEN
  if (order == 1):
    setColor(Color.blue)
    moveTo(z, t)
  else:
    u = (x + z - t + y) / 2;
    v = (y + t + z - x) / 2;
    dragon(order - 1, x, y, u, v);
    dragonInverse(order - 1, u, v, z, t);
  # END HIDDEN
# END TEMPLATE

dragon(getParam(0), getParam(1), getParam(2), getParam(3), getParam(4))
