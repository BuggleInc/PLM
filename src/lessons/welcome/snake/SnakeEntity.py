# BEGIN SOLUTION
def endingPosition():
	if not isFacingWall():
		return False
	else:
		res = False
		turnLeft()
		if isFacingWall():
			res = True
		turnRight()
		return res

def snakeStep():
	if isFacingWall():
		if getDirection().toString() == "EAST":
			turnLeft()
			forward()
			turnLeft()
		else:
			turnRight()
			forward()
			turnRight()
	else:
		forward()

brushDown()
while not endingPosition():
	snakeStep()
# END SOLUTION
