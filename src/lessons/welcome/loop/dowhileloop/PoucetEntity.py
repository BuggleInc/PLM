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

def crossing():
    return (entity.getX() % 5 == 1) and ( entity.getY()%5==1 )
    
def exitReached():
    return getGroundColor().equals(Color.orange)

# BEGIN SOLUTION
while not exitReached() :
    seen = 0
    within = False
    
    while not within or not crossing():
        within = True
        forward()
        if isOverBaggle():
            seen += 1
    
    if seen > 2:
        turnLeft()
    else:
        turnRight()
forward()
# END SOLUTION
