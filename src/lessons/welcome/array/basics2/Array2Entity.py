def setX(i):
	errorMsg("Sorry Dave, I cannot let you run setX(i) in this exercise")

def setY(i):
	errorMsg("Sorry Dave, I cannot let you run setY(i) in this exercise")
def setPos(x,y):
	errorMsg("Sorry Dave, I cannot let you run setPos(x,y) in this exercise")

# BEGIN TEMPLATE
# BEGIN SOLUTION
def mark(color):
	setBrushColor(color)
	brushDown()
	brushUp()

colors = []

# read the colors
for i in range(getWorldHeight()-1):
	colors.append(getGroundColor())
	forward()
colors.append(getGroundColor())
backward(getWorldHeight()-1)

def makeLine (colors):
	offset = int(readMessage())
	mark(colors[(0+offset)% colors.__len__()])
	for i in range(getWorldWidth()-1):
		forward()
		mark(colors[(i+1+offset)% colors.__len__()])
	backward(getWorldWidth()-1)

# duplicate the pattern
for i in range(getWorldWidth()-1):
	turnLeft()
	forward()
	turnRight()
	makeLine(colors)

# END SOLUTION
# END TEMPLATE
