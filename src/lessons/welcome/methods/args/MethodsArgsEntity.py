def run():
    global forward, backward
    _forward  = forward
    _backward = backward

    def forward(i=-1):
        if i!=-1:
            errorMsg("forward(int) forbidden in this exercise")
        else:
            _forward()

    def backward(i=-1):
        if i!=-1:
            errorMsg("backward(int) forbidden in this exercise")
        else:
            _backward()

    # BEGIN SOLUTION 
    def move(nbPas, doforward):
        if doforward:
            for i in range(nbPas):
                forward()
        else:
            for i in range(nbPas):
                backward()
    # END SOLUTION

    move(getY(), getDirection() == Direction.NORTH) 
