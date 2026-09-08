from RemoteBuggle import *
def run():
    global forward, backward
    _forward  = forward
    _backward = backward

    def forward(i=1):
        if i==1:
          _forward()
        else:
          errorMsg("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.")
    def backward(i=1):
        if i==1:
          _backward()
        else:
          errorMsg("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead.")

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
