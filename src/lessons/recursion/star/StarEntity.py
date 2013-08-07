# BEGIN SOLUTION 

BRANCH_COUNT = 5

def branch(size):
	forward(size);
	right(360 / BRANCH_COUNT)
	forward(size)

	for i in range(2):
		left(360 / BRANCH_COUNT)


def star(size, c):
	setColor(c)
	for i in range(BRANCH_COUNT):
		branch(size)

star(100, Color.black)
right(45)
star(80, Color.blue)
right(45)
star(60, Color.red)
# END SOLUTION
