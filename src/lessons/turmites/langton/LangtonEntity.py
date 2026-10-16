from RemoteBuggle import *

def run():
    # BEGIN TEMPLATE
    def tick():
        # BEGIN SOLUTION
        if getGroundColor() == Color.white:
            right()
            setBrushColor(Color.black)
            brushDown()
            brushUp()
            step()
        else:
            left()
            setBrushColor(Color.white)
            brushDown()
            brushUp()
            step()
        # END SOLUTION
    # END TEMPLATE
            
    for i in range(getParamInt(0)):
        tick()
        stepDone()
