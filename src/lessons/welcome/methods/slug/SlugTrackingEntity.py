from RemoteBuggle import *
def run():
   # BEGIN TEMPLATE
   def isFacingTrail():
      # write your code here
      # BEGIN SOLUTION
      if isFacingWall():
         return False
      else:
         step()
         res = (getGroundColor() == Color.green)
         stepBack()
         return res
      # END SOLUTION
   # END TEMPLATE
   
   def hunt():
      while not isOverBaggle():
         brushUp()
         if isFacingTrail():
            brushDown()
            step()
            brushUp()
         else:
            left()
      pickupBaggle()
   
   hunt()
