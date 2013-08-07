# BEGIN TEMPLATE
def tree(steps, length, angle, shrink):
  # BEGIN SOLUTION
  if (steps <= 0):
    pass# do nothing 
  else:
    forward(length)
    right(angle)
    tree(steps-1, length*shrink, angle, shrink)
    left(2*angle)
    tree(steps-1, length*shrink, angle, shrink)
    right(angle)
    backward(length)
  # END SOLUTION
# END TEMPLATE

tree(getParam(0),getParam(1),getParam(2),getParam(3))
