# BEGIN SOLUTION
for i in range(getValueCount()-1):
	min = i
	for j in range(i+1, getValueCount()):
		if isSmaller(j,min):
			min = j
	# Swap the smallest unsorted element into the end of the sorted list. 
	swap(min,i);
# END SOLUTION
