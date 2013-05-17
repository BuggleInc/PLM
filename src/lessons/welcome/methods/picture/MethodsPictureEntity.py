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


makeV(Color.yellow)
makeV(Color.red)
makeV(Color.blue)
makeV(Color.green)
# END TEMPLATE
