# BEGIN SOLUTION
def mark():
   brushDown()
   brushUp()

def makeV():
   forward(2)
   mark()
   forward()
   left()
   forward()
   mark()
   backward()
   right()
   forward()
   mark()   
   forward(2)
   left()

def makePattern():
    for i in range(4):
        makeV()
    forward(7)

def makeLine(count):
   for i in range(count):
      makePattern()
   backward(count*7)

def nextLine():
   left()
   forward(7)
   right()

for i in range(9):
   makeLine(9)
   nextLine()
# END SOLUTION
