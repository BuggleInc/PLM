# BEGIN TEMPLATE
def sierpinski(level, length):
    # BEGIN SOLUTION
    if (level >= 0):
      for i in range(3):
        forward(length / 2.)
        turnRight(360. / 3.)
        sierpinski(level-1, length / 2.)
        turnLeft(360. / 3.)
        forward(length / 2.)
        turnRight(360. / 3.)
    # END SOLUTION
# END TEMPLATE
sierpinski(getParam(0), getParam(1))
