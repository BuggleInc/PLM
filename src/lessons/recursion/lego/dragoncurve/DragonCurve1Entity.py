from RemoteTurtle import *

def run():
  # BEGIN TEMPLATE
  def dragon(order, x, y, z, t):
    # BEGIN SOLUTION 
      if order == 1:
        setPos(x, y)
        moveTo(z, t)
      else:
        u = (x + z + t - y) / 2
        v = (y + t - z + x) / 2;
        dragon(order - 1, x, y, u, v)
        dragon(order - 1, z, t, u, v)
  # END SOLUTION
  # END TEMPLATE
  
  dragon(getParamInt(0), getParamDouble(1), getParamDouble(2), getParamDouble(3), getParamDouble(4))
