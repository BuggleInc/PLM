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
   left()
   forward()
   mark()
   backward()
   right()
   forward()
   mark()   
   forward()
   left()


makeV(Color.yellow)
makeV(Color.red)
makeV(Color.blue)
makeV(Color.green)
# END SOLUTION
