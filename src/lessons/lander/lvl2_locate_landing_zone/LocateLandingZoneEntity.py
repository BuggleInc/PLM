# BEGIN TEMPLATE
def getLandingZone():
    # BEGIN SOLUTION
    lastPoint = getGround()[0]
    for point in getGround():
        if (point != lastPoint):
            if lastPoint[1] == point[1]:
                return (lastPoint[0],point[0])
        lastPoint = point
    # END SOLUTION
# END TEMPLATE 

targetStart = 0;
targetEnd = 0;

def initialize():
    global targetStart
    global targetEnd
    (targetStart, targetEnd) = getLandingZone()
    if (targetStart > targetEnd):
        print ("Your starting point is after your ending point ("+str(targetStart)+">"+str(targetEnd)+"). Bad things will happen")

def step():
    if (getX() < targetStart):
        setDesiredAngle(-30)
    elif (getX() > targetEnd):
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

initialize()
while isFlying():
  step()
  simulateStep()
