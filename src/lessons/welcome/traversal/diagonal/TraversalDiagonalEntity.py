def forward(i):
	errorMsg("Sorry Dave, I cannot let you use forward() in this exercise")

def backward(i):
	errorMsg("Sorry Dave, I cannot let you use backward() in this exercise")

def right(i):
	errorMsg("Sorry Dave, I cannot let you use right() in this exercise")

def left(i):
	errorMsg("Sorry Dave, I cannot let you use left() in this exercise")

def back(i):
	errorMsg("Sorry Dave, I cannot let you use back() in this exercise")

def isFacingWall(i):
	errorMsg("Sorry Dave, I cannot let you use isFacingWall() in this exercise")

def isBackingWall(i):
	errorMsg("Sorry Dave, I cannot let you use isFacingWall() in this exercise")

# BEGIN TEMPLATE
# BEGIN SOLUTION
cpt = 0
diag = 0
writeMessage(cpt)
while not ((getX() == getWorldWidth() - 1) and (getY() == getWorldHeight() - 1)):
	x = getX()
	y = getY()
	if ((x < getWorldWidth() - 1) and (y > 0)):
		x += 1
		y -= 1
	elif (diag < getWorldHeight() - 1):	
		diag += 1
		y = diag
		x = 0
	else:
		diag += 1
		x = diag - (getWorldWidth() - 1)
		y = diag - x
	setPos(x, y)
	cpt += 1
	writeMessage(cpt)
# END SOLUTION 
# END TEMPLATE 
