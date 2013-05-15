import java.awt.Color as Color

# BEGIN TEMPLATE
def isFacingTrail(trailColor):
   # write your code
   # BEGIN SOLUTION
   if isFacingWall():
      return False
   else:
	  forward()
	  res = getGroundColor().toString() == trailColor.toString()
	  backward()
	  return res
# END TEMPLATE

def hunt():
   while not isOverBaggle():
      brushUp()
      if isFacingTrail(Color.green):
         brushDown()
         forward()
         brushUp()
      else:
	     turnLeft()
   pickUpBaggle()

hunt()

