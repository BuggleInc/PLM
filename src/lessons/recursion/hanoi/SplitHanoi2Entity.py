# BEGIN TEMPLATE
def splitHanoi(height, src, other, dst1,dst2):
	# Your code here
	# BEGIN SOLUTION
	if height > 0:
		splitHanoi(height-1, src,dst1,dst2,other);
	  	move(src,dst1);
	  	hanoi(height-1, dst2,src,dst1);
	  	move(src,dst2);
	  	hanoi(height-1, other,src,dst2);

def hanoi(height, src,other,dst):	
	if height > 0:
		hanoi(height-1, src,dst,other);
		move(src,dst);
		hanoi(height-1, other,src,dst);
	# END SOLUTION
# END TEMPLATE
splitHanoi(getSlotSize(getParam(0))/2, getParam(0),getParam(1),getParam(2),getParam(3))