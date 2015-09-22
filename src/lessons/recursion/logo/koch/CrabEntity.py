# BEGIN TEMPLATE 
import math

def crab(levels, length):
		# BEGIN SOLUTION 
		if levels == 0:
			forward(length)
		else:
			left(45)
			crab(levels-1, length/math.sqrt(2))
			right(90)
			crab(levels-1, length/math.sqrt(2))
			left(45)
		# END SOLUTION	

# END TEMPLATE

crab(getParam(0), getParam(1))
