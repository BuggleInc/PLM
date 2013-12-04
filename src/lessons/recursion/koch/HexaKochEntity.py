# BEGIN TEMPLATE 

def hexaKoch(levels, length):
		# BEGIN SOLUTION 
		if levels == 0:
			forward(length)
		else:
			hexaKoch(levels-1, length*.14)
			left(120)
			for i in range(5):
				hexaKoch(levels-1, length*.14)
				right(60)
			left(180)
			hexaKoch(levels-1, length*.14)
		# END SOLUTION	

def drawCurve(levels, length):
	hexaKoch(levels, length)

# END TEMPLATE

drawCurve(getParam(0), getParam(1))
