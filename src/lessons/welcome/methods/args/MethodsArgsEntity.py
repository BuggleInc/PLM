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
def move(nbPas, doforward):
    if doforward:
        for i in range(nbPas):
            forward()
    else:
        for i in range(nbPas):
            backward()
#END TEMPLATE

move(getY(), entity.getDirection().toString() == "NORTH") 
