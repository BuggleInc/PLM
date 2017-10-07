import java.awt.Color as Color

# BEGIN TEMPLATE
def isFacingTrail():
   # write your code here
   # BEGIN SOLUTION
   if isFacingWall():
      return False
   else:
	  forward()
	  res = (getGroundColor() == Color.green)
	  backward()
	  return res
   # END SOLUTION
# END TEMPLATE

def hunt():
   while not isOverBaggle():
      brushUp()
      if isFacingTrail():
         brushDown()
         forward()
         brushUp()
      else:
	     left()
   pickupBaggle()

hunt()

