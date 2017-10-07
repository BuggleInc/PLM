# BEGIN TEMPLATE
def move3(height, src, mid, dst):
	# Your code here
	# BEGIN SOLUTION
	if (height>0):
		move3(height-1, src, dst, mid);
		move(src,dst);
		move(src,dst);
		move(src,dst);
		move3(height-1, mid, src, dst);
	# END SOLUTION
# END TEMPLATE
move3(getSlotSize(getParam(0))/3, getParam(0),getParam(1),getParam(2))