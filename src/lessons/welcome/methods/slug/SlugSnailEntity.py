from RemoteBuggle import *

def run():
   # BEGIN TEMPLATE
   def isFacingTrail(color):
      # write your code here
      # BEGIN SOLUTION
      if isFacingWall():
         return False
      else:
         step()
         res = (getGroundColor() == color)
         stepBack()
         return res
      # END SOLUTION
   
   def hunt(color):
      # BEGIN SOLUTION
      while not isOverBaggle():
         brushUp()
         if isFacingTrail(color):
            brushDown()
            step()
            brushUp()
         else:
            left()
      pickupBaggle()
      # END SOLUTION
   # END TEMPLATE
   
   hunt(getColorIntParam())
