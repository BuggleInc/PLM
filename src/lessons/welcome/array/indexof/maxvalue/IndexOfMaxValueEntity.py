
# BEGIN TEMPLATE
# Computes the index of the maximum of the values contained in tab variable
def indexOfMaximum(tab):
	# BEGIN SOLUTION 
	max = tab[0]
	index = 0
	for i in range(len(tab)):
		if tab[i] > max:
			max = tab[i]
			index = i
	return index
	# END SOLUTION
# END TEMPLATE	

setResult( indexOfMaximum(getValues()) )