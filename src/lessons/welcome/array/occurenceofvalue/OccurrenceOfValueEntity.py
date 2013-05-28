# BEGIN TEMPLATE
# Counts the occurrences of the value 'lookingFor' contained in tab variable
def indexOf(tab,lookingFor):
	# BEGIN SOLUTION
	count = 0
	for i in range(len(tab)):
		if tab[i] == lookingFor:
			count += 1
	return count
# END SOLUTION
# END TEMPLATE

setResult( indexOf(getValues(), 17) )
