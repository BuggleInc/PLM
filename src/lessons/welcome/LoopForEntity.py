def forward(i=-1):
    if i==-1:
      entity.forward()
    else:
      errorMsg("Sorry Dave, I cannot let you use forward with argument")
def backward(i=-1):
    if i==-1:
      entity.backward()
    else:
      errorMsg("Sorry Dave, I cannot let you use backward with argument")
# BEGIN SOLUTION
cpt = 0

while isOverBaggle() == False:
  cpt = cpt+1
  forward()
pickUpBaggle()
for i in range(cpt):
  backward()
dropBaggle()
# END TEMPLATE
