def forward(i):
	errorMsg("forward(int) forbidden in this exercise")

def backward(i):
	errorMsg("backward(int) forbidden in this exercise")

def run():
	for i in range(7):
		if haveBaggle():
			return
		turnRight()
		forward()
		turnLeft()

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

