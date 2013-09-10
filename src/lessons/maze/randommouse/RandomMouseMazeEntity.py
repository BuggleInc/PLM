import java.util.Random
rand = java.util.Random()

def random3():
    n = rand.nextInt(3)
    if n<0.33:
        return 0
    elif n <0.66:
        return 1
    return 2

def setX(i):
        errorMsg("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead.")
def setY(i):
        errorMsg("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead.")
def setPos(x,y):
        errorMsg("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead.")

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
