import java.awt.Color as Color

# BEGIN SOLUTION
def mark():
   brushDown()
   brushUp()

def squareA(c):
   setBrushColor(c)
   forward()
   mark()
   turnLeft()
   forward()
   turnLeft()
   forward()
   mark()
   turnLeft()
   forward()
   turnLeft()
   
def squareB(c):
   setBrushColor(c)
   mark()
   forward()
   turnLeft()
   forward()
   mark()
   turnLeft()
   forward()
   turnLeft()
   forward()
   turnLeft()

def bigSquare():
   squareA(Color.red)
   forward(2)
   squareB(Color.blue)
   backward(2)
   turnLeft()
   forward(2)
   turnRight()
   squareB(Color.yellow)
   forward(2)
   squareA(Color.green)
   backward(2)
   turnLeft()
   backward(2)
   turnRight()
	
bigSquare()
forward(4)
bigSquare()
		
backward(4)
turnLeft()
forward(4)
turnRight()
		
bigSquare() 
forward(4)
bigSquare()
# END SOLUTION

