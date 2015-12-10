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

def crossing():
    here  = entity.world.getCell(getX(),getY());
    right = entity.world.getCell( (getX()+1)%entity.world.getWidth() ,  getY()  )
    below = entity.world.getCell( getX()  ,  (getY()+1)%entity.world.getHeight())
        
    open = 0
    if not here.hasLeftWall():
        open += 1;
    if not here.hasTopWall():
        open += 1;
    if not right.hasLeftWall():
        open += 1;
    if not below.hasTopWall():
        open += 1;
        
    return open>2 or (here.hasLeftWall() != right.hasLeftWall()) or (here.hasTopWall() != below.hasTopWall());
    
def exitReached():
    return getGroundColor().equals(Color.orange)

# BINDINGS TRANSLATION 
def croisement():
    return crossing()
def sortieTrouvee():
    return exitReached()


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
        left()
    else:
        right()
forward()
# END SOLUTION
