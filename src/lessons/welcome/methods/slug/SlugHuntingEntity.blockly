import java.awt.Color as Color

# BEGIN TEMPLATE
# Copy your isFacingTrail, and write your code below
# BEGIN SOLUTION
def isFacingTrail():
   if isFacingWall():
      return False
   else:
	  forward()
	  res = getGroundColor() == Color.green
	  backward()
	  return res

while not isOverBaggle():
   brushUp()
   if isFacingTrail():
      brushDown()
      forward()
      brushUp()
   else:
     left()
pickupBaggle()
# END SOLUTION
# END TEMPLATE
