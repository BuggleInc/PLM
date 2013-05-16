# BEGIN TEMPLATE 

def snowSide(levels, length):
		# BEGIN SOLUTION 
		if levels == 0:
			forward(length)
		else:
			snowSide(levels-1, length/3)
			turnLeft(60)
			snowSide(levels-1, length/3)
			turnRight(120)
			snowSide(levels-1, length/3)
			turnLeft(60)
			snowSide(levels-1, length/3)
		# END SOLUTION	

def snowFlake (levels, length):
	snowSide(levels, length)
	turnRight(120)
	snowSide(levels, length)
	turnRight(120)
	snowSide(levels, length)
	turnRight(120)
# END TEMPLATE

snowFlake(getParam(0), getParam(1))
