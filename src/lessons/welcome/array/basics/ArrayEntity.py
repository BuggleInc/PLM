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
for i in range(getWorldHeight()):
	colors.append(getGroundColor())
	forward()

def makeLine (colors):
	for i in range(getWorldWidth()):
		mark(colors[i])
		forward()

# duplicate the pattern
for i in range(getWorldWidth()-1):
	turnLeft()
	forward()
	turnRight()
	forward()
	makeLine(colors)

# END SOLUTION
# END TEMPLATE
