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

spiral(getParam(0),getParam(1),getParam(2),getParam(3))
