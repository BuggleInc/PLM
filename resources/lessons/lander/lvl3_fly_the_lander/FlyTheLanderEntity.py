# BEGIN HIDDEN
(startPos,endPos) = (0,0)
# END HIDDEN

# BEGIN TEMPLATE
def initialize():
    pass
# BEGIN SOLUTION
    global startPos
    global endPos
    lastPoint = getGround()[0]
    for point in getGround():
        if (point != lastPoint):
            if lastPoint[1] == point[1]:
                (startPos,endPos) = (lastPoint[0],point[0])
        lastPoint = point
# END SOLUTION

def step():
    pass
# BEGIN HIDDEN
    if (getX() < startPos):
        setDesiredAngle(-30)
    elif (getX() > endPos):
        setDesiredAngle(30)
    elif (getSpeedX() > 5):
        setDesiredAngle(25)
    elif (getSpeedX() < -5):
        setDesiredAngle(-25)
    else:
        setDesiredAngle(0);
        
    if (getSpeedY() <-9):
        setDesiredThrust(4)
    else:
        setDesiredThrust(3)

# END HIDDEN

# END TEMPLATE

initialize()
while isFlying():
  step()
  simulateStep()
