
# BEGIN TEMPLATE
# Do not change these definitions
STOP   = 0
NOTURN = 1
LEFT   = 2
BACK   = 4
RIGHT  = 8

NEXT_COLOR = 0
NEXT_MOVE  = 1
NEXT_STATE = 2

# The step method will update this variable
state = 0

def step(rule, colors):
	currentColor = 0
	global state # we use within the method that variable which lives outside. Thus the use of the global keyword. 
	# BEGIN SOLUTION
	for i in range( len(colors) ):
		if getGroundColor() == colors[i]:
			currentColor = i
			
	setBrushColor(colors[ rule[state][currentColor][NEXT_COLOR] ])
	brushDown()
	brushUp()
	
	if (rule[state][currentColor][NEXT_MOVE] == STOP):
		pass # nothing
	elif (rule[state][currentColor][NEXT_MOVE] == NOTURN):
		forward()
	elif (rule[state][currentColor][NEXT_MOVE] == LEFT):
		left()
		forward()
	elif (rule[state][currentColor][NEXT_MOVE] == RIGHT):
		right()
		forward()
	elif (rule[state][currentColor][NEXT_MOVE] == BACK):
		back()
		forward()
	else:
		log("Unknown turn command associated to i=%d: %d" % (currentColor, rule[state][currentColor][NEXT_MOVE]))

	state = rule[state][currentColor][NEXT_STATE]
	
	# END SOLUTION
# END TEMPLATE

allColors = [Color.white, Color.black, Color.blue, Color.cyan, Color.green, Color.orange, Color.red, Color.gray, Color.magenta, Color.darkGray, Color.pink, Color.lightGray]

rule=getParam(1)
colors = []
for i in range( len(rule) ):
	colors.append(allColors[i])

for i in range(getParam(0)):
	step(rule,colors)
	stepDone()
