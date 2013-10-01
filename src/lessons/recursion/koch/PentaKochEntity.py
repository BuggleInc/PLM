# BEGIN TEMPLATE 

def pentaKoch(levels, length):
		# BEGIN SOLUTION 
		if levels == 0:
			forward(length)
		else:
			squareSide(levels-1, length*.4)
			left(90)
			squareSide(levels-1, length*.4)
			right(90)
			squareSide(levels-1, length*.4)
			right(90)
			squareSide(levels-1, length*.4)
			left(90)
			squareSide(levels-1, length*.4)
		# END SOLUTION	

# END TEMPLATE

snowSquare(getParam(0), getParam(1))
