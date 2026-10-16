from RemoteBuggle import *
def run():
    # BEGIN SOLUTION 
    def isDirectionFree(dir):
        memo = getDirection()
        setDirection(dir)
        res = not isFacingWall()
        setDirection(memo)
        return res

    def stepHandOnWall():
        # PRE: we have a wall on the left
        # POST: we still have the same wall on the left, are one step ahead
        while not isFacingWall():
            step()
            left() # change to right to get a right follower
        right() # change to left to get a right follower

    northRunner = True
    chosenDir = Direction.NORTH
    setDirection(chosenDir)

    while not isOverBaggle():
        if northRunner:
            while not isFacingWall():
                step()
            right()
            northRunner = False
        else: # left follower mode
            stepHandOnWall()
            if isDirectionFree(chosenDir) and getDirection() == chosenDir:
                northRunner = True

    pickupBaggle()

    # END SOLUTION
