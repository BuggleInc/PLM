import java.awt.Color as Color

# BEGIN SOLUTION
def mark():
   brushDown()
   brushUp()

def makeV(c):
   setBrushColor(c)
   forward()
   mark()
   forward()
   turnLeft()
   forward()
   mark()
   backward()
   turnRight()
   forward()
   mark()   
   forward()
   turnLeft()

def makePattern():
   makeV(Color.yellow)
   makeV(Color.red)
   makeV(Color.blue)
   makeV(Color.green)
   forward(5)

def makeLine(count):
   for i in range(count):
      makePattern()
   backward(count*5)

def nextLine():
   turnLeft()
   forward(5)
   turnRight()

for i in range(3):
   makeLine(3)
   nextLine()
# END TEMPLATE
