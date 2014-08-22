def forward(i=1):
    if i==1:
      entity.forward()
    else:
      errorMsg("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.")
def backward(i=1):
    if i==1:
      entity.backward()
    else:
      errorMsg("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead.")
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
