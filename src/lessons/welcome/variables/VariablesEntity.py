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
while not isOverBaggle():
  cpt += 1 
  forward()
pickUpBaggle()
while cpt>0:
  backward()
  cpt -= 1
dropBaggle()
# END SOLUTION
