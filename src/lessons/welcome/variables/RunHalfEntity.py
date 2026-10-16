from RemoteBuggle import *
def run():
    def isOverOrange():
        return getGroundColor() == Color.orange

    # BEGIN SOLUTION
    baggle = 0
    orange = 0
    while 2 * baggle != orange + 1:
        step()
        if isOverBaggle():
            baggle += 1
        if isOverOrange():
            orange += 1
    # END SOLUTION 
