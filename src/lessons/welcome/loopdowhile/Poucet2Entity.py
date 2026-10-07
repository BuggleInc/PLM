from RemoteBuggle import *
def run():
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
