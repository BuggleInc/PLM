//RemoteBuggle

int hasRightWall(int x, int y);
int hasBottomWall(int x, int y);
int setValueIfLess(int x, int y, int val) ;
void evaluatePaths();
void followShortestPath();

/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	evaluatePaths(); // write on each case the distance to the maze exit
	followShortestPath(); // make the buggle follow the shortest path
	pickupBaggle(); // enjoy the baggle!
}
// tools functions
int hasRightWall(int x, int y) {
	return hasLeftWall((x + 1) % getWorldWidth(), y);
}

int hasBottomWall(int x, int y) {
	return hasTopWall(x, (y + 1) % getWorldHeight());
}


int setValueIfLess(int x, int y, int val) {
	int existing = getIndication(x, y);
	if (val < existing) {
		setIndication(x, y, val);
		return 1;
	}
	return 0;
}

void evaluatePaths() {
	// looking for labyrinth exit

	int x,y;
	for (x = 0; x < getWorldWidth(); x++){
		for (y = 0; y < getWorldHeight(); y++){
			if (hasBaggle(x,y)){
				setIndication(x, y, 0);
			}
		}
	}

	int changed = 1;
	while (changed) {
		changed = 0;
		for (x = 0; x < getWorldWidth(); x++) {
			for (y = 0; y < getWorldHeight(); y++) {
				int indication = getIndication(x, y);
				if (indication != 9999) {
					if (! hasBottomWall(x,y))
						changed |= setValueIfLess(x, (y + 1) % getWorldHeight(), indication + 1);

					if (! hasRightWall(x,y))
						changed |= setValueIfLess((x + 1) % getWorldWidth(), y, indication + 1);

					if (! hasTopWall(x,y))
						changed |= setValueIfLess(x, (y+getWorldHeight() - 1) % getWorldHeight(), indication + 1);

					if (! hasLeftWall(x,y))
						changed |= setValueIfLess((x +getWorldWidth() - 1) % getWorldWidth(), y, indication + 1);

				}
			}
		}
	}
}

void followShortestPath() {
	while (! isOverBaggle()) {

		int x = getX();
		int y = getY();

		int topValue = 9999;
		int bottomValue = 9999;
		int leftValue = 9999;
		int rightValue = 9999;

		if (! hasTopWall(x, y))
			topValue = getIndication(x, (y + getWorldHeight() - 1) % getWorldHeight());

		if (! hasBottomWall(x, y))
			bottomValue = getIndication(x, (y+1) % getWorldHeight());

		if (! hasLeftWall(x, y))
			leftValue = getIndication((x +getWorldWidth() - 1) % getWorldWidth(), y);

		if (! hasRightWall(x, y))
			rightValue = getIndication((x + 1) % getWorldWidth(), y);

		if (topValue <= bottomValue && topValue <= leftValue && topValue <= rightValue)
			setDirection(NORTH);
		else if (rightValue <= topValue && rightValue <= bottomValue && rightValue <= leftValue)
			setDirection(EAST);
		else if (leftValue <= rightValue && leftValue <= bottomValue && leftValue <= topValue)
			setDirection(WEST);
		else if (bottomValue <= topValue && bottomValue <= rightValue && bottomValue <= leftValue)
			setDirection(SOUTH);

		forward(1);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */
