# BEGIN TEMPLATE
def splitHanoi(height, src, other, dst1,dst2):
	# Your code here
	# BEGIN SOLUTION
	if height > 0:
	  	moveDouble(height-1, src,dst1,dst2,other);
	 	move(src,dst1);
	  	move(src,dst2);
	  	splitHanoi(height-1, other,src,dst1, dst2);

	
def moveDouble(height, src, other1, other2, dst):	
	if height > 0:
		moveDouble(height-1, src,other1,dst,other2);
	  	move(src,other1);
	  	move(src,dst);
	  	move(other1,dst);
	  	moveDouble(height-1, other2, src, other1,dst);
	# END SOLUTION
# END TEMPLATE
splitHanoi(getSlotSize(getParam(0))/2, getParam(0),getParam(1),getParam(2),getParam(3))