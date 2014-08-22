# BEGIN SOLUTION
def endingPosition():
	if not isFacingWall():
		return False
	else:
		res = False
		left()
		if isFacingWall():
			res = True
		right()
		return res

def snakeStep():
	if isFacingWall():
		if getDirection().toString() == "EAST":
			left()
			forward()
			left()
		else:
			right()
			forward()
			right()
	else:
		forward()

brushDown()
while not endingPosition():
	snakeStep()
# END SOLUTION
