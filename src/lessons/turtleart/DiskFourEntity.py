# BEGIN SOLUTION
def quadrant():
	for i in range(90):
		forward(100);
		backward(100);
		right(1);

quadrant();
setColor(Color.RED);
quadrant();
setColor(Color.ORANGE);
quadrant();
setColor(Color.MAGENTA);
quadrant();
# END SOLUTION
