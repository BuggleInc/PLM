from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    for i in range(7):
        for side in range(4):
            for k in range(4):
                step()
            left();
            for k in range(2):
                step()
            right();
            for k in range(4):
                step()
            right()
            step()
            step()
            left()
            for k in range(4):
                step()
            left()
    # END SOLUTION
