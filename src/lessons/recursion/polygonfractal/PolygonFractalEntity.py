# BEGIN TEMPLATE
def polygonFractal (levels, sides, length, shrink):
    # BEGIN SOLUTION
    if (levels == 0):
      /* do nothing */
    else:
      for i in range(sides):
        forward(length);
        
        turnLeft((sides-2)*360/(sides*2))
        polygonFractal(levels-1, sides, length*shrink,shrink)
        turnRight((sides-2)*360/(sides*2))
        turnRight(360/sides)
    # END SOLUTION
# END TEMPLATE 

polygonFractal(getParam(0),getParam(1),getParam(2),getParam(3))
