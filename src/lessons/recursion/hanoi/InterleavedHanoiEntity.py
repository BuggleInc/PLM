# BEGIN TEMPLATE
def interleavedHanoi(height, src1,src2, other, dst):
	# Your code here
	# BEGIN SOLUTION
	if height > 0:
	  hanoi(height-1, src1,dst,other)
	  move(src1,dst)
	  hanoi(height-1, src2,dst,src1)
	  move(src2,dst)
	  interleavedHanoi(height-1, other,src1,src2, dst)

	
def hanoi(height, src,other,dst):	
	if height > 0:
		hanoi(height-1, src,dst,other);
		move(src,dst);
		hanoi(height-1, other,src,dst);
	# END SOLUTION
# END TEMPLATE
interleavedHanoi(getSlotSize(getParam(0)), getParam(0),getParam(1),getParam(2),getParam(3))