# BEGIN TEMPLATE 

def snowSide(levels, length):
		# BEGIN SOLUTION 
		if levels == 0:
			forward(length)
		else:
			snowSide(levels-1, length/3)
			left(60)
			snowSide(levels-1, length/3)
			right(120)
			snowSide(levels-1, length/3)
			left(60)
			snowSide(levels-1, length/3)
		# END SOLUTION	

def snowFlake (levels, length):
	snowSide(levels, length)
	right(120)
	setColor(Color.blue)
	snowSide(levels, length)
	right(120)
	setColor(Color.orange)
	snowSide(levels, length)
	right(120)
# END TEMPLATE

snowFlake(getParam(0), getParam(1))
