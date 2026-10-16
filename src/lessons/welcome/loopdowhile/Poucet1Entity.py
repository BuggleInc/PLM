from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    while not exitReached() :
        seen = 0
        within = False
        
        while not within or not crossing():
            within = True
            step()
            if isOverBaggle():
                seen += 1
        
        if seen > 2:
            left()
        else:
            right()
    step()
    # END SOLUTION
