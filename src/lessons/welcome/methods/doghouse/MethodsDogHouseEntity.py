line = -1

def turnRight():
	raise java.lang.RuntimeException("turnRight() forbidden in this exercise.");

# does not work as Java stacktrace does not contain python function name ;(
#def turnLeft():
#    global line
#    errorMsg(str(line))
#    for s in java.lang.Thread.currentThread().getStackTrace():
#        if "turnLeft" in s.getMethodName():
#            if line != -1 and line != s.getLineNumber():
#                errorMsg("Forbidden to use turnLeft() more than once in this exercise.")
#                # throw new RuntimeException("Forbidden to use turnLeft() more than once in this exercise.");
#            else:
#                line = s.getLineNumber()
#                entity.turnLeft()	
	
def dogHouse():
	raise java.lang.RuntimeException("You must define a dogHouse() method.");

# BEGIN SOLUTION
def dogHouse():
    for i in range(4):
        forward()
        forward()
        turnLeft()

# END TEMPLATE

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
