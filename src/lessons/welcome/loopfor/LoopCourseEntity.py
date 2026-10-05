from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    for i in range(10):
        for side in range(4):
            for step in range(8):
                forward()
            left()
    # END SOLUTION
