# BEGIN TEMPLATE
def solve(src,dst,other, height=-1):
	if height==-1: # initial case
		height=getSlotSize(src) 
	
	# Your code here
	# BEGIN SOLUTION
	if height != 0:
		solve(src,other,dst, height-1);
		move(src,dst);
		solve(other,dst,src, height-1);
	# END SOLUTION
# END TEMPLATE
solve(getParam(0),getParam(1),getParam(2))