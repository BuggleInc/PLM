def setX(i):
	errorMsg("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead.")

def setY(i):
	errorMsg("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead.")
def setPos(x,y):
	errorMsg("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead.")

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
	left()
	forward()
	right()
	forward()
	makeLine(colors)

# END SOLUTION
# END TEMPLATE
