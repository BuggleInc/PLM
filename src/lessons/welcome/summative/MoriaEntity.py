from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    back()
    while not isFacingWall():
        while (not isOverBaggle()) and (not isFacingWall()):
            step()
        if isOverBaggle():
            pickupBaggle()
            back()
            while not isOverBaggle():
                step()
            stepBack()
            dropBaggle()
            back()
            step()
    right()
    step()
    left()
    step()
    # END SOLUTION
