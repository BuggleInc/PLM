# BEGIN SOLUTION
i = 0
while i < getValueCount()-1:
	if isSmaller(i,i+1):
		i += 1
	else:
		swap(i,i+1)
		i -=  1
	if i == -1:
		i=1 # Remaining at 0 would not mean "move forward" as stated in the mission text
# END SOLUTION
