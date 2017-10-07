# BEGIN TEMPLATE 

def squareSide(levels, length):
		# BEGIN SOLUTION 
		if levels == 0:
			forward(length)
		else:
			squareSide(levels-1, length/3)
			left(90)
			squareSide(levels-1, length/3)
			right(90)
			squareSide(levels-1, length/3)
			right(90)
			squareSide(levels-1, length/3)
			left(90)
			squareSide(levels-1, length/3)
		# END SOLUTION	

def snowSquare (levels, length):
	squareSide(levels, length)
	right(90)
	setColor(Color.blue)
	squareSide(levels, length)
	right(90)
	setColor(Color.orange)
	squareSide(levels, length)
	right(90)
	setColor(Color.magenta)
	squareSide(levels, length)
	right(90)
# END TEMPLATE

snowSquare(getParam(0), getParam(1))
