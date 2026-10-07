from RemoteBuggle import *
def run():
    # BEGIN SOLUTION

    def stepHandOnWall():
        nonlocal angleSum
        while not isFacingWall():
            forward()
            left()
            angleSum += 1
        right()
        angleSum -= 1

    def isDirectionFree(dir):
        memo = getDirection()
        setDirection(dir)
        res = not isFacingWall()
        setDirection(memo)
        return res

    northRunner = True
    chosenDir = Direction.NORTH
    setDirection(chosenDir)
    angleSum =  0

    while not isOverBaggle():
        if northRunner:
            while not isFacingWall():
                forward()
            right()
            angleSum -= 1
            northRunner = False
        else :
            stepHandOnWall()
            if isDirectionFree(chosenDir) and angleSum == 0:
                northRunner = True
    pickupBaggle()
        
    # END SOLUTION
