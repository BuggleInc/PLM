def forward(i=1):
    if i>1:
        errorMsg("Sorry Dave, I cannot let you use forward(i>1) in this exercise")
    entity.forward()
def backward(i=1):
    if i>1:
        errorMsg("Sorry Dave, I cannot let you use backward(i>1) in this exercise")
    entity.backward()

# BEGIN SOLUTION
cpt = 0
while cpt != 4:
	forward()
	if isOverBaggle():
		cpt += 1
# END SOLUTION 
