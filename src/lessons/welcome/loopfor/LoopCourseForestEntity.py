from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    for i in range(7):
        for side in range(4):
            for step in range(4):
                forward()
            left();
            for step in range(2):
                forward()
            right();
            for step in range(4):
                forward()
            right()
            forward()
            forward()
            left()
            for step in range(4):
                forward()
            left()
    # END SOLUTION
