# BEGIN TEMPLATE
def hanoi(initialPos, increasing):
	
	# Your code here
	# BEGIN SOLUTION
	count=0
	small=initialPos
	p1=initialPos
	p2=initialPos
	while getSlotSize(p1)!=0 or getSlotSize(p2)!=0:
		if (count%2 == 0):
			if increasing:
				next = (small+1)%3
			else:
				next = (small-1+3)%3
			move(small,next)
			small = next
		if small == 0:
			p1=1
			p2=2
		elif small == 1:
			p1=0
			p2=2
		else:
			p1=0
			p2=1
		if (count%2 == 1):
			if (getSlotRadius(p1)> getSlotRadius(p2)):
				move(p2,p1)
			else:
				move(p1,p2)
		count += 1			
	# END SOLUTION
# END TEMPLATE
hanoi(getParam(0), getParam(1))