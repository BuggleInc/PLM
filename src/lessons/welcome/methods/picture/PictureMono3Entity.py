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

def makePattern():
    for i in range(4):
        makeV()
    forward(5)

def makeLine(count):
   for i in range(count):
      makePattern()
   backward(count*5)

def nextLine():
   left()
   forward(5)
   right()

for i in range(9):
   makeLine(9)
   nextLine()
# END SOLUTION
