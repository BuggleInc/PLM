def forward(i=-1):
    if i!=-1:
        errorMsg("forward(int) forbidden in this exercise")
    else:
        entity.forward()

def backward(i=-1):
    if i!=-1:
        errorMsg("backward(int) forbidden in this exercise")
    else:
        entity.backward()

# BEGIN SOLUTION 
def move(nbPas, goForward):
    if goForward:
        for i in range(nbPas):
            forward()
    else:
        for i in range(nbPas):
            backward()
# END SOLUTION

move(getY(), entity.getDirection() == Direction.NORTH) 
