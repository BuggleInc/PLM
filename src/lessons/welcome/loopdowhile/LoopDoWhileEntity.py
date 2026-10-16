from RemoteBuggle import *
def run():
    def isGroundWhite():
        return getGroundColor() == Color.white

    # BEGIN SOLUTION
    more = True
    while more:
        step()
        if isGroundWhite():
      	     more = False 
    # END SOLUTION
