# BEGIN TEMPLATE
def haveBaggle():
    # BEGIN SOLUTION 
	res = False
	for i in range(6):
		if isOverBaggle():
			res = True
		forward()
        backward(6)
	return res
    # END SOLUTION
#END TEMPLATE

for i in range(7):
    if haveBaggle():
        break
    right()
    forward()
    left()

