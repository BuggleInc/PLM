from RemoteBuggle import *
def run():
    # BEGIN SOLUTION
    done = False
    while not done:
        c = readMessage()
        if c == 'R':
            right()
            step()
        elif c == 'L':
            left()
            step()
        elif c == 'I':
            back()
            step()
        elif c == 'A':
            step()
        elif c == 'B':
            forward(2)
        elif c == 'C':
            forward(3)
        elif c == 'Z':
            stepBack()
        elif c == 'Y':
            backward(2)
        elif c == 'X':
            backward(3)
        else:        
            done = True
    # END SOLUTION

