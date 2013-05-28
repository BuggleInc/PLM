# BEGIN TEMPLATE
# Computes the maximum of the values contained in tab variable
def maximum(tab):
	# BEGIN SOLUTION
	max=tab[0]
	for i in range(len(tab)):
		if tab[i] >= max:
			max = tab[i]
	return max
# END SOLUTION
# END TEMPLATE	

setResult(maximum(getValues()))