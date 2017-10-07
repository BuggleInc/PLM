def forward(i=1):
    if i>1:
        errorMsg("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.")
    entity.forward()
def backward(i=1):
    if i>1:
        errorMsg("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead.")
    entity.backward()

# BEGIN SOLUTION 
cpt = 0
while not isOverBaggle():
  cpt += 1 
  forward()
pickupBaggle()
while cpt>0:
  backward()
  cpt -= 1
dropBaggle()
# END SOLUTION
