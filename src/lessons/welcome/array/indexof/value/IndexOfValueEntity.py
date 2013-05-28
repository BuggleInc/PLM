# BEGIN TEMPLATE
# Computes the index of the first value equals to 'lookingFor' contained in tab variable
def indexOf(tab,lookingFor):
	# BEGIN SOLUTION
	for i in range(len(tab)):
		if tab[i] == lookingFor:
			return i
	return -1
# END SOLUTION
# END TEMPLATE

setResult( indexOf(getValues(), 17) )