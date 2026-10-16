from RemoteBuggle import *
def run():
    # BEGIN SOLUTION 
    def move(nbPas, doforward):
        if doforward:
            for i in range(nbPas):
                step()
        else:
            for i in range(nbPas):
                stepBack()
    # END SOLUTION

    move(getY(), getDirection() == Direction.NORTH) 
