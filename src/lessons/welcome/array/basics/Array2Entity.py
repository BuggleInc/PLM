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
	left()
	forward()
	right()
	makeLine(colors)

# END SOLUTION
# END TEMPLATE
