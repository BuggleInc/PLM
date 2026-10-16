from RemoteBuggle import *
def run():
    # BEGIN SOLUTION 
    def stepHandOnWall():
        # PRE: we have a wall on the left
        # POST: we still have the same wall on the left, are one step ahead
        while not isFacingWall():
            step()
            left() # change to right to get a right follower
        right() # change to left to get a right follower

    # make sure that we have a wall to the left
    left()
    while not isFacingWall():
        step()
    right()

    while not isOverBaggle():
        stepHandOnWall()
    pickupBaggle()
    # END SOLUTION
