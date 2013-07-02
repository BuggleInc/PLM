# BEGIN SOLUTION 
begin = 0
end = getValueCount()-2
swapped = True
while swapped  and   end-begin>1:
	swapped = False
	for i in range(begin, (end+1) ):
		if not isSmaller(i,i+1):
			swap(i,i+1)
			swapped = True
	end -= 1
	for i in range(end,begin-1,-1):
		if not isSmaller(i,i+1):
			swap(i,i+1)
			swapped = True
	begin += 1
# END SOLUTION
