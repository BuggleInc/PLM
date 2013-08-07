def forward(i):
	errorMsg("I'm sorry Dave, I'm afraid I cannot let you use forward() in this exercise.")

def backward(i):
	errorMsg("I'm sorry Dave, I'm afraid I cannot let you use backward() in this exercise.")

def right(i):
	errorMsg("I'm sorry Dave, I'm afraid I cannot let you use right() in this exercise.")

def left(i):
	errorMsg("I'm sorry Dave, I'm afraid I cannot let you use left() in this exercise.")

def back(i):
	errorMsg("I'm sorry Dave, I'm afraid I cannot let you use back() in this exercise.")

def isFacingWall(i):
	errorMsg("I'm sorry Dave, I'm afraid I cannot let you use isFacingWall() in this exercise.")

def isBackingWall(i):
	errorMsg("I'm sorry Dave, I'm afraid I cannot let you use isFacingWall() in this exercise.")

# BEGIN SOLUTION

def nextStep():
	x = getX()
	y = getY()
	
	if (x < getWorldWidth()-1):
		x += 1
	else:
		x = 0 
		if (y < getWorldHeight()-1):
			y += 1
		else:
			y = 0
	setPos(x,y)

cpt=0;
writeMessage(cpt)
while not (getX() == getWorldWidth()-1 and getY() == getWorldHeight()-1):
	nextStep();
	cpt += 1
	writeMessage(cpt);
# END SOLUTION