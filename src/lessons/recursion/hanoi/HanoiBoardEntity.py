# BEGIN TEMPLATE
def hanoi(height, src,other,dst):
	
	# Your code here
	# BEGIN SOLUTION
	if height != 0:
		hanoi(height-1, src,dst,other);
		move(src,dst);
		hanoi(height-1, other,src,dst);
	# END SOLUTION
# END TEMPLATE
hanoi(getSlotSize(getParam(0)), getParam(0),getParam(1),getParam(2))