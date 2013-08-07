import java.awt.Color as Color

# BEGIN SOLUTION
def mark():
   brushDown()
   brushUp()

def squareA(c):
   setBrushColor(c)
   forward()
   mark()
   left()
   forward()
   left()
   forward()
   mark()
   left()
   forward()
   left()
   
def squareB(c):
   setBrushColor(c)
   mark()
   forward()
   left()
   forward()
   mark()
   left()
   forward()
   left()
   forward()
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

