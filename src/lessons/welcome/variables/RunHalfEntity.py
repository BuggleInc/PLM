from RemoteBuggle import *
def run():
    def isOverOrange():
        return getGroundColor() == Color.orange

    def estSurOrange(): # BINDINGS TRANSLATION
        return isOverOrange()

    # BEGIN SOLUTION
    baggle = 0
    orange = 0
    while 2 * baggle != orange + 1:
        forward()
        if isOverBaggle():
            baggle += 1
        if isOverOrange():
            orange += 1
    # END SOLUTION 
