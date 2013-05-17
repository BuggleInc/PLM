# BEGIN TEMPLATE
def solve(src,dst,height=-1):
	if height==-1: # initial case
		height=getSlotSize(src) 
	
	# Your code here
	# BEGIN SOLUTION
	if height != 0:
		if src+dst==1:   # 0+1
			other=2
		if src+dst==2:   # 0+2
			other=1;
		if src+dst==3:   # 1+2
			other=0;

		solve(src,other, height-1);
		move(src,dst);
		solve(other,dst,height-1);
	# END SOLUTION
# END TEMPLATE
solve(getParam(0),getParam(1))