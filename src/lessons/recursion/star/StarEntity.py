# BEGIN SOLUTION 

BRANCH_COUNT = 5

def branch(size):
	forward(size);
	turnRight(360 / BRANCH_COUNT)
	forward(size)

	for i in range(2):
		turnLeft(360 / BRANCH_COUNT)


def star(size, c):
	setColor(c)
	for i in range(BRANCH_COUNT):
		branch(size)

star(100, Color.black)
turnRight(45)
star(80, Color.blue)
turnRight(45)
star(60, Color.red)
# END SOLUTION
