import java.awt.Color
def isGroundWhite():
  return getGroundColor().equals(java.awt.Color.white)

# BEGIN SOLUTION
more = True
while more:
  forward()
  if isGroundWhite():
  	more = False 
# END TEMPLATE
