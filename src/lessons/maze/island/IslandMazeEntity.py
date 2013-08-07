def setX(i):
        errorMsg("Sorry Dave, I'm afraid I cannot let you use setX(i) in this exercise")
def setY(i):
        errorMsg("Sorry Dave, I'm afraid I cannot let you use setY(i) in this exercise")
def setPos(x,y):
        errorMsg("Sorry Dave, I'm afraid I cannot let you use setPos(x,y) in this exercise")


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
        forward()
        left() # change to right to get a right follower
    right() # change to left to get a right follower

northRunner = True
chosenDir = Direction.NORTH
setDirection(chosenDir)

while not isOverBaggle():
    if northRunner:
        while not isFacingWall():
            forward()
        right()
        northRunner = False
    else: # left follower mode
        stepHandOnWall()
        if isDirectionFree(chosenDir) and getDirection() == chosenDir:
            northRunner = True

pickupBaggle()

# END SOLUTION
