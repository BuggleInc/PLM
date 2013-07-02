# BEGIN SOLUTION
i = 0
while i < getValueCount()-1:
	if isSmaller(i,i+1):
		i += 1
	else:
		swap(i,i+1)
		i -=  1
	if i == -1:
		i=0
# END SOLUTION
