from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    def dogHouse():
        for i in range(4):
            step()
            step()
            left()

    # END SOLUTION

    brushDown()
    dogHouse()
    brushUp()    
    forward(4)
    brushDown()
    dogHouse()		
    brushUp()
    forward(2)
    left()
    forward(4)
    brushDown()
    dogHouse()		
    brushUp()
    forward(2)
    left()
    forward(4)
    brushDown()
    dogHouse()
