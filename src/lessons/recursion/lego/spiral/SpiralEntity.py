from RemoteTurtle import *

def run():
  # BEGIN TEMPLATE
  def spiral(steps, angle, length, increment):
    # BEGIN SOLUTION
    if (steps <= 0):
      pass# do nothing
    else:
      forward(length)
      left(angle)
      spiral(steps-1, angle, length+increment, increment)
    # END SOLUTION
  # END TEMPLATE
  
  spiral(getParamInt(0),getParamInt(1),getParamInt(2),getParamInt(3))
