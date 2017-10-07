# BEGIN SOLUTION
swapped = True
while swapped:
	swapped = False
	for i in range(getValueCount()-1):
		if (not isSmaller(i,i+1)):
			swap(i,i+1)
			swapped = True
# END SOLUTION
