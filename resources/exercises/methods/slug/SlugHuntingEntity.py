import java.awt.Color as Color

# BEGIN TEMPLATE
def hunt():
    # Complete this method
    # BEGIN SOLUTION
    while not isOverBaggle():
        brushUp()
        if isFacingTrail():
            brushDown()
            forward()
            brushUp()
        else:
            left()
    pickupBaggle()
def isFacingTrail():
   if isFacingWall():
      return False
   else:
	  forward()
	  res = getGroundColor() == Color.green
	  backward()
	  return res
# END SOLUTION

# and copy your isFacingTrail() over
# END TEMPLATE
hunt()
