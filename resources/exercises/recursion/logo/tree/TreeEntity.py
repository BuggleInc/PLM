def setX(i):
	errorMsg("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead.")
def setY(i):
	errorMsg("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead.")
def setPos(x,y):
	errorMsg("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead.")

colors = [Color.cyan,      Color.blue,   Color.magenta, 
          Color.orange,    Color.yellow, Color.green,
          Color.lightGray, Color.gray,   Color.darkGray,   Color.black, Color.red]

def current(i):
    if (i>=len(colors) or i < 0):
        setColor(Color.red)
    else:
        setColor(colors[i])

def subtree(steps, length, angle, shrink):
  if (steps != 0):
    setColor(Color.black)
    forward(length)
    right(angle)
    subtree(steps-1, length*shrink, angle, shrink)
    left(2*angle)
    subtree(steps-1, length*shrink, angle, shrink)
    right(angle)
    backward(length)

# BEGIN TEMPLATE
def tree(steps, length, angle, shrink):
  # BEGIN SOLUTION
  if (steps != 0):
    current(steps)
    forward(length)
    right(angle)
    tree(steps-1, length*shrink, angle, shrink)
    left(2*angle)
    tree(steps-1, length*shrink, angle, shrink)
    right(angle)
    current(steps)
    backward(length)
  # END SOLUTION
# END TEMPLATE

tree(getParam(0),getParam(1),getParam(2),getParam(3))
