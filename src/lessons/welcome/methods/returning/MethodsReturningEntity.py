def forward(i=-1):
    if i!=-1:
        errorMsg("forward(int) forbidden in this exercise")
    else:
        entity.forward()

def backward(i=-1):
    if i!=-1:
        errorMsg("backward(int) forbidden in this exercise")
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

