from RemoteBuggle import *
def run():
    # BEGIN TEMPLATE
    def hunt():
        # Complete this method
        # BEGIN SOLUTION
        while not isOverBaggle():
            brushUp()
            if isFacingTrail():
                brushDown()
                step()
                brushUp()
            else:
                left()
        pickupBaggle()
    def isFacingTrail():
       if isFacingWall():
          return False
       else:
          step()
          res = getGroundColor() == Color.green
          stepBack()
          return res
    # END SOLUTION
    
    # and copy your isFacingTrail() over
    # END TEMPLATE
    hunt()
