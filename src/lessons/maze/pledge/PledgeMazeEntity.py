def setX(i):
        errorMsg("Sorry Dave, I'm afraid I cannot let you use setX(i) in this exercise")
def setY(i):
        errorMsg("Sorry Dave, I'm afraid I cannot let you use setY(i) in this exercise")
def setPos(x,y):
        errorMsg("Sorry Dave, I'm afraid I cannot let you use setPos(x,y) in this exercise")

# BEGIN SOLUTION

def stepHandOnWall():
    global angleSum
    while not isFacingWall():
        forward()
        turnLeft()
        angleSum += 1
    turnRight()
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
        turnRight()
        angleSum -= 1
        northRunner = False
    else :
        stepHandOnWall()
        if isDirectionFree(chosenDir) and angleSum == 0:
            northRunner = True
pickUpBaggle()
    
# END SOLUTION
