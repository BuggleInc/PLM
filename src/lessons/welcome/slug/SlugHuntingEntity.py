import java.awt.Color as Color

# BEGIN TEMPLATE
# BEGIN HIDDEN
def isFacingTrail():
   if isFacingWall():
      return False
   else:
	  forward()
	  res = getGroundColor().toString() == Color.green.toString()
	  backward()
	  return res
# END HIDDEN

def hunt():
   # write your code here
   # BEGIN SOLUTION
   while not isOverBaggle():
      brushUp()
      if isFacingTrail():
         brushDown()
         forward()
         brushUp()
      else:
	     turnLeft()
   pickupBaggle()
   # END SOLUTION
# END TEMPLATE

hunt()
