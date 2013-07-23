def forward(i=1):
    if i>1:
        errorMsg("Sorry Dave, I cannot let you use forward(i>1) in this exercise")
    entity.forward()
def backward(i=1):
    if i>1:
        errorMsg("Sorry Dave, I cannot let you use backward(i>1) in this exercise")
    entity.backward()
def isOverOrange():
    return getGroundColor() == Color.orange

# BEGIN SOLUTION
baggle = 0
orange = 0
while 2 * baggle != orange + 1:
    forward()
    if isOverBaggle():
        baggle += 1
    if isOverOrange():
        orange += 1
# END SOLUTION 
