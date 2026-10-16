from RemoteBuggle import *

def run():
    # BEGIN TEMPLATE
    # BEGIN SOLUTION
    def mark(color):
        setBrushColor(color)
        brushDown()
        brushUp()
    
    colors = []
    
    # read the colors
    for i in range(getWorldHeight()):
        colors.append(getGroundColor())
        step()
    
    def makeLine (colors):
        for i in range(getWorldWidth()):
            mark(colors[i])
            step()
    
    # duplicate the pattern
    for i in range(getWorldWidth()-1):
        left()
        step()
        right()
        step()
        makeLine(colors)
    
    # END SOLUTION
    # END TEMPLATE
