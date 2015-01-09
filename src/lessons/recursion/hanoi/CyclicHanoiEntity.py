def move(src,dst):
	entity.cyclicMove(src,dst)

# BEGIN TEMPLATE
def clockwise(height, src,mid,dst):
	
	# Your code here
	# BEGIN SOLUTION
	if height != 0:
		anti(height-1,src,dst,mid);
		move(src,dst);
		anti(height-1,mid,src,dst);
def anti(height, src,mid,dst):
	if height != 0:
		anti(height-1,src,mid,dst);
		move(src,mid);
		clockwise(height-1,dst,mid,src);			
		move(mid,dst);
		anti(height-1,src,mid,dst);
	# END SOLUTION
# END TEMPLATE
clockwise(getSlotSize(getParam(0)), getParam(0),getParam(1),getParam(2))