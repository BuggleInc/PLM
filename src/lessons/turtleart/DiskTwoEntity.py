# BEGIN SOLUTION
def quadrant():
	for i in range(20):
		forward(100);
		backward(100);
		right(1);

for i in range(9):
	setColor(Color.BLACK);
	quadrant();
	setColor(Color.RED);
	quadrant();
# END SOLUTION
