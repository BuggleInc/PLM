# BEGIN SOLUTION
turnBack()
while not isFacingWall():
    while (not isOverBaggle()) and (not isFacingWall()):
        forward()
    if isOverBaggle():
        pickUpBaggle()
        turnBack()
        while not isOverBaggle():
            forward()
        backward()
        dropBaggle()
        turnBack()
        forward()
turnRight()
forward()
turnLeft()
forward()
# END SOLUTION
