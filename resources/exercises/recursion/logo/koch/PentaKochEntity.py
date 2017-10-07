# BEGIN TEMPLATE 

def pentaKoch(levels, length):
		# BEGIN SOLUTION 
		if levels == 0:
			forward(length)
		else:
			pentaKoch(levels-1, length*.4)
			left(108);
			for i in range(4):
				pentaKoch(levels-1, length*.4);
				right(72);
			left(180);
			pentaKoch(levels-1, length*.4)
		# END SOLUTION	

# END TEMPLATE

pentaKoch(getParam(0), getParam(1))
