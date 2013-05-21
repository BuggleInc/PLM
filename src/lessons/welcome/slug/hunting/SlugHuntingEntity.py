import java.awt.Color as Color

def isFacingTrail(trailColor):
   if isFacingWall():
      return False
   else:
	  forward()
	  res = getGroundColor().toString() == trailColor.toString()
	  backward()
	  return res

# BEGIN TEMPLATE

def hunt():
   # write your code here
   # BEGIN SOLUTION
   while not isOverBaggle():
      brushUp()
      if isFacingTrail(Color.green):
         brushDown()
         forward()
         brushUp()
      else:
	     turnLeft()
   pickUpBaggle()
   # END SOLUTION
# END TEMPLATE

hunt()
