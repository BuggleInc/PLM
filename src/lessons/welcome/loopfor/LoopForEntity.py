def forward(i=1):
    if i>1:
      errorMsg("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.")
    else:
      entity.forward()
def backward(i=1):
    if i>1:
      errorMsg("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead.")
    else:
      entity.backward()
# BEGIN SOLUTION
cpt = 0

while not isOverBaggle():
  cpt = cpt+1
  forward()
pickupBaggle()
for i in range(cpt):
  backward()
dropBaggle()
# END SOLUTION
