def move(src,dst):
	if (src==0 and dst==2) or (src==2 and dst==0):
		errorMsg("Sorry Dave, I cannot let you use move disks between slots 0 and 2 directly. Use the intermediate slot in all moves.")
	else:
		entity.move(src,dst)
# BEGIN TEMPLATE
def linearHanoi(height, src,mid,dst):
	
	# Your code here
	# BEGIN SOLUTION
	if height != 0:
		linearHanoi(height-1, src,mid,dst);
		move(src,mid);
		linearHanoi(height-1, dst,mid,src);
		move(mid,dst);
		linearHanoi(height-1, src,mid,dst);
	# END SOLUTION
# END TEMPLATE
linearHanoi(getSlotSize(getParam(0)), getParam(0),getParam(1),getParam(2))