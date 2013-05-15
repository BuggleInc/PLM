line = -1

def turnRight():
      errorMsg("turnRight() forbidden in this exercise.")

# does not work as Java stacktrace does not contain python function name ;(
#def turnLeft():
#    global line
#    errorMsg(str(line))
#    for s in java.lang.Thread.currentThread().getStackTrace():
#        if "turnLeft" in s.getMethodName():
#            if line != -1 and line != s.getLineNumber():
#                errorMsg("Forbiden to use turnLeft() more than once in this exercise.")
#                # throw new RuntimeException("Forbiden to use turnLeft() more than once in this exercise.");
#            else:
#                line = s.getLineNumber()
#                entity.turnLeft()	
	

def run():
        brushDown()
        dogHouse()
        brushUp()    
        forward(4)
        brushDown()
        dogHouse()		
        brushUp()
        forward(2)
        turnLeft()
        forward(4)
        brushDown()
        dogHouse()		
        brushUp()
        forward(2)
        turnLeft()
        forward(4)
        brushDown()
        dogHouse()

def dogHouse():
	errorMsg("You must define a dogHouse() method")

# BEGIN SOLUTION
def dogHouse():
    for i in range(4):
        forward()
        forward()
        turnLeft()

run()
# END SOLUTION
