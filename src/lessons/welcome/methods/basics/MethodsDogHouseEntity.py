line = -1

def right():
	raise java.lang.RuntimeException("Sorry Dave, I cannot let you use right() in this exercise. Use left() instead.");

# does not work as Java stacktrace does not contain python function name ;(
#def left():
#    global line
#    errorMsg(str(line))
#    for s in java.lang.Thread.currentThread().getStackTrace():
#        if "left" in s.getMethodName():
#            if line != -1 and line != s.getLineNumber():
#                errorMsg("Forbidden to use left() more than once in this exercise.")
#                # throw new RuntimeException("Forbidden to use left() more than once in this exercise.");
#            else:
#                line = s.getLineNumber()
#                entity.left()	
	
def dogHouse():
	raise java.lang.RuntimeException("You must define a dogHouse() method.");

# BEGIN SOLUTION
def dogHouse():
    for i in range(4):
        forward()
        forward()
        left()

# END SOLUTION

brushDown()
dogHouse()
brushUp()    
forward(4)
brushDown()
dogHouse()		
brushUp()
forward(2)
left()
forward(4)
brushDown()
dogHouse()		
brushUp()
forward(2)
left()
forward(4)
brushDown()
dogHouse()
