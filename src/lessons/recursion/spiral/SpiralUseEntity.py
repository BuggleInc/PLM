def spiral(steps, angle, length, increment):
  if (steps <= 0):
    pass
  else:
    forward(length)
    left(angle)
    spiral(steps-1, angle, length+increment, increment)
  
# BEGIN TEMPLATE
spiral(100,91,1,2)
# END TEMPLATE
