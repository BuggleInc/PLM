from RemoteLander import *
def run():
    (startPos,endPos) = (0,0)
    
    # BEGIN TEMPLATE
    def initialize():
    # BEGIN SOLUTION
        nonlocal startPos
        nonlocal endPos
        lastPoint = getGround()[0]
        for point in getGround():
            if (point != lastPoint):
                if lastPoint.y == point.y:
                    return (lastPoint.x,point.x)
            lastPoint = point
        return (0, 0)
        # END SOLUTION
    # END TEMPLATE
    
    def step():
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
    
    
    initialize()
    while isFlying():
      step()
      simulateStep()
