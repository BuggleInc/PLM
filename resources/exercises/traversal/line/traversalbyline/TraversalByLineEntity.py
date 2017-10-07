def forward(i):
	errorMsg("Sorry Dave, I cannot let you use forward() in this exercise. Use setPos(x,y) instead.")

def backward(i):
	errorMsg("Sorry Dave, I cannot let you use backward() in this exercise. Use setPos(x,y) instead.")

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