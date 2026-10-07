from RemoteBuggle import *
def run():
    import random

    def random3():
        return random.randrange(0,3)

    # BEGIN SOLUTION
    while not isOverBaggle():
        n = random3()
        if n == 0:
            if not isFacingWall():
                forward()
        elif n == 1:
            left()
        else:
            right()
    pickupBaggle()
    # END SOLUTION
