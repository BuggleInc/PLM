def forward(i):
    if i>1:
        errorMsg("Sorry Dave, I cannot let you use forward(i>1) in this exercise")

def backward(i):
    if i>1:
        errorMsg("Sorry Dave, I cannot let you use backward(i>1) in this exercise")

# BEGIN SOLUTION 
cpt = 0
while isOverBaggle() == False:
  cpt = cpt +1
  forward()
pickUpBaggle()
while cpt>0:
  backward()
  cpt = cpt-1
dropBaggle()
# END SOLUTION
