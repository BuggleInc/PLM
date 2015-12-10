import java.awt.Color as Color

# BEGIN TEMPLATE
def isFacingTrail(color):
   # write your code here
   # BEGIN SOLUTION
   if isFacingWall():
      return False
   else:
	  forward()
	  res = (getGroundColor() == color)
	  backward()
	  return res
   # END SOLUTION

def hunt(color):
   # BEGIN HIDDEN
   while not isOverBaggle():
      brushUp()
      if isFacingTrail(color):
         brushDown()
         forward()
         brushUp()
      else:
	     left()
   pickupBaggle()
   # END HIDDEN
# END TEMPLATE

hunt(getParam(0))

