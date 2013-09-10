def setX(i):
        errorMsg("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead.")
def setY(i):
        errorMsg("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead.")
def setPos(x,y):
        errorMsg("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead.")

# BEGIN SOLUTION

def stepHandOnWall():
    global angleSum
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
