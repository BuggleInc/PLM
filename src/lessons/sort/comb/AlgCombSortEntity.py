# BEGIN SOLUTION
gap = getValueCount()
swapped = True
while gap > 1 or swapped:
	if gap > 1:
		gap = int( gap / 1.3)
	swapped = False
	i = 0
	while i+gap <  getValueCount():
		if not isSmaller(i,i+gap):
			swap(i, i+gap)
			swapped = True
		i += 1
# END SOLUTION
