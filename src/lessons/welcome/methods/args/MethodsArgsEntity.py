from RemoteBuggle import *
def run():
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
