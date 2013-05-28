# BEGIN TEMPLATE
# Computes the average value of the values contained in tab variable
def average(tab):
	# BEGIN SOLUTION
	sum = 0
	for i in range(len(tab)):
		sum += tab[i]
	return sum / len(tab)
	# END SOLUTION 
# END TEMPLATE

setResult( average(getValues()) )