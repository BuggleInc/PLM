def forward(i):
	errorMsg("Sorry Dave, I cannot let you use forward() in this exercise. Use setPos(x,y) instead.")

def backward(i):
	errorMsg("Sorry Dave, I cannot let you use backward() in this exercise. Use setPos(x,y) instead.")

# BEGIN TEMPLATE
# BEGIN SOLUTION
def nextStep():
	x=getX()
	y=getY()

	if y % 2 == 0:
		if x < getWorldWidth()-1:
			x += 1
		elif y < getWorldHeight()-1:
			y += 1
	else:
		if 0 < x:
			x -= 1
		elif y < getWorldHeight()-1:
			y += 1 
	setPos(x,y)

def endingPosition():
	return (getX() == getWorldWidth() -1) and (getY() == getWorldHeight()-1)

cpt = 0
writeMessage(cpt)
while not endingPosition():
	nextStep()
	cpt += 1
	writeMessage(cpt)
# END SOLUTION
# END TEMPLATE
