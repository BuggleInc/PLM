from RemoteBuggle import *
def run():
    def isGroundWhite():
        return getGroundColor() == Color.white

    def estSurBlanc(): # BINDINGS TRANSLATION
        return isGroundWhite() 

    # BEGIN SOLUTION
    more = True
    while more:
        forward()
        if isGroundWhite():
      	     more = False 
    # END SOLUTION
