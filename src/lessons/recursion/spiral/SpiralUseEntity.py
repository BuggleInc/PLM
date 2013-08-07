def spiral(steps, angle, length, increment):
  if (steps <= 0):
    pass
  else:
    forward(length)
    left(angle)
    spiral(steps-1, angle, length+increment, increment)
  
# BEGIN TEMPLATE
def doit(page):
  # BEGIN SOLUTION
  if page == 0:
    spiral(100,90+1,1,2)
  elif page == 1:
    spiral(100,120+1,1,2)
  elif page == 2:
    spiral(5,360/5,100,0)
  elif page == 3:
    spiral(5,2*360/5,150,0)
  elif page == 4:
    spiral(360,1,1,0)  
  # END SOLUTION
# END TEMPLATE
  
doit(getParam(0))
