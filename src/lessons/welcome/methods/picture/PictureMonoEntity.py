# BEGIN SOLUTION
def mark():
   brushDown()
   brushUp()

def makeV():
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


for i in range(4):
    makeV()
# END SOLUTION
