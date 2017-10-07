# BEGIN TEMPLATE
def tricolor(height, src, mid, dst):
	
	# Your code here
	# BEGIN SOLUTION
	gather(height, src, mid, dst);
	move3(height, dst,mid,src);
	move3(height, src,dst,mid);
	scatter(height, mid,dst,src);
	
def gather(height, src, mid, dst):
	if (height >0):
		gather(height-1,src,mid,dst);
		move(src,mid);
		move3(height-1, dst,mid,src);
		move(mid,dst);
		move(mid,dst);
		move3(height-1, src, mid, dst);
def move3(height, src, mid, dst):
	if (height>0):
		move3(height-1, src, dst, mid);
		move(src,dst);
		move(src,dst);
		move(src,dst);
		move3(height-1, mid, src, dst);
		
def scatter(height, src, mid, dst):
	if (height>0):
		move3(height-1, src, dst,mid);
		move(src,dst);
		move(src,dst);
		move3(height-1, mid,dst,src);
		move(dst,mid);
		scatter(height-1, src,mid,dst);
	# END SOLUTION
# END TEMPLATE
tricolor(getSlotSize(getParam(0)), getParam(0),getParam(1),getParam(2))