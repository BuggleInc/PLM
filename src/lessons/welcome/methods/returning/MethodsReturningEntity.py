def forward(i=-1):
    if i!=-1:
        errorMsg("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.")
    else:
        entity.forward()

def backward(i=-1):
    if i!=-1:
        errorMsg("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead.")
    else:
        entity.backward()


# BEGIN TEMPLATE
def haveBaggle():
    # BEGIN SOLUTION 
	res = False
	for i in range(6):
		if isOverBaggle():
			res = True
		forward()
	for i in range(6):
		backward()
	return res
    # END SOLUTION
#END TEMPLATE

for i in range(7):
    if haveBaggle():
        break
    right()
    forward()
    left()

