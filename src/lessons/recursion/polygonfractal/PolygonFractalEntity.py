# BEGIN TEMPLATE
def polygonFractal (levels, sides, length, shrink):
    # BEGIN SOLUTION
    if (levels == 0):
      pass
    else:
      for i in range(sides):
        forward(length)
        
        left((sides-2)*360/(sides*2))
        polygonFractal(levels-1, sides, length*shrink,shrink)
        right((sides-2)*360/(sides*2))
        right(360/sides)
    # END SOLUTION
# END TEMPLATE 

polygonFractal(getParam(0),getParam(1),getParam(2),getParam(3))
