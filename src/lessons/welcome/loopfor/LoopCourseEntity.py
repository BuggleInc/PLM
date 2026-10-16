from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    for i in range(10):
        for side in range(4):
            for k in range(8):
                step()
            left()
    # END SOLUTION
