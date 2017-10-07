def setX(i):
        errorMsg("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead.")
def setY(i):
        errorMsg("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead.")
def setPos(x,y):
        errorMsg("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead.")

# BEGIN SOLUTION 
def stepHandOnWall():
    # PRE: we have a wall on the left
    # POST: we still have the same wall on the left, are one step ahead
    while not isFacingWall():
        forward()
        left() # change to right to get a right follower
    right() # change to left to get a right follower

# make sure that we have a wall to the left
left()
while not isFacingWall():
    forward()
right()

while not isOverBaggle():
    stepHandOnWall()
pickupBaggle()
# END SOLUTION
