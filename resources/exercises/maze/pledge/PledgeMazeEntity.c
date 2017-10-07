//RemoteBuggle


void stepHandOnWall();
int isChosenDirectionFree();

int angleSum;
Direction chosenDirection = NORTH;

/* BEGIN TEMPLATE */

void run() {
/* BEGIN SOLUTION */
	int state = 0 ;
	angleSum = 0;
	setDirection(chosenDirection);
	while ( !isOverBaggle() ){
		switch ( state ){
		case 0: // North runner mode
			while ( !isFacingWall() )
				forward(1);

			right(); // make sure that we have a left wall
			angleSum--;
			state = 1; // time to enter the Left Follower mode
			break;
		case 1: // Left Follower Mode
			stepHandOnWall(); // follow the left wall
			if ( isChosenDirectionFree() && angleSum == 0  )
				state =0; // time to enter in North Runner mode

			break;
		}
	}
	pickupBaggle();
}


void stepHandOnWall(){
	while ( ! isFacingWall() ){
		forward(1);
		left();
		angleSum++;
	}
	right();
	angleSum--;
}


int isChosenDirectionFree() {
	Direction memorizedD = getDirection();
	setDirection(chosenDirection);
	int isFree = !isFacingWall();
	setDirection(memorizedD);
	return isFree;

	/* END SOLUTION */
}
/* END TEMPLATE */
