
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
	

allColors = [Color.white, Color.yellow, Color.red, Color.cyan, Color.green, Color.orange, 
			Color.blue, Color.black,
			Color.gray, Color.magenta, Color.darkGray, Color.pink, Color.lightGray]

# BEGIN TEMPLATE
# Your code should be something like 
#  nbSteps = 42;
#  rule = [[ [0, NOTURN, 0], [0, NOTURN, 0] ]];

# You can change the initial position of the turmite with setX()/setY() methods

# You can possibly have more states (ie, bigger second dimension), and more colors (ie, bigger third --internal-- dimension) 
# Naturally, less boring turmites are easy to achieve since this one runs forward endlessly 		  

# BEGIN SOLUTION
nbStep = 8342
rule =  [[ [1, LEFT, 0], [1, LEFT, 1]], [[0, NOTURN, 0], [0, NOTURN, 1]]]
setX(8)
setY(33)
# END SOLUTION
# END TEMPLATE

colors = []
for i in range( min( len(rule), len(allColors) ) ):
	colors.append(allColors[i])
	
if len(rule) > len(allColors): # allColors is too short; create the other colors randomly
	for offset in range (len(rule) - len(allColors)):
		newColor = null
		while newColor == null:
			newColor = Color(Math.random()*255. , Math.random()*255. , Math.random()*255. )
			for c in colors:
				if c == newColor: # Damn we already picked that color; take another one please
					newColor = null
		colors.append(newColor)

for i in range(nbStep):
	step(rule,colors)
	stepDone()
