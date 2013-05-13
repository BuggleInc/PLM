# BEGIN SOLUTION
finished = False
while not finished:
    #c = getIndication()

    if isOverMessage():
        c = readMessage()[0]
    else:
        c = ''

    if c == 'R':
        turnRight()
        forward()
    elif c == 'L':
        turnLeft()
        forward()
    elif c == 'I':
        turnBack()
        forward()
    elif c == 'A':
        forward()
    elif c == 'B':
        forward(2)
    elif c == 'C':
        forward(3)
    elif c == 'Z':
        backward()
    elif c == 'Y':
        backward(2)
    elif c == 'X':
        backward(3)
    else:        
        finished = True
# END SOLUTION

