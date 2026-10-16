from RemoteBuggle import *
def run():

    # BEGIN SOLUTION
    def mark():
       brushDown()
       brushUp()

    def squareA(c):
       setBrushColor(c)
       step()
       mark()
       left()
       step()
       left()
       step()
       mark()
       left()
       step()
       left()
       
    def squareB(c):
       setBrushColor(c)
       mark()
       step()
       left()
       step()
       mark()
       left()
       step()
       left()
       step()
       left()

    def bigSquare():
       squareA(Color.red)
       forward(2)
       squareB(Color.blue)
       backward(2)
       left()
       forward(2)
       right()
       squareB(Color.yellow)
       forward(2)
       squareA(Color.green)
       backward(2)
       left()
       backward(2)
       right()
    	
    bigSquare()
    forward(4)
    bigSquare()
    		
    backward(4)
    left()
    forward(4)
    right()
    		
    bigSquare() 
    forward(4)
    bigSquare()
    # END SOLUTION

