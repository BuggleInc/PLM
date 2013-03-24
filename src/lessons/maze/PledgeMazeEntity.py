# BEGIN SOLUTION
  public void run() {
		chosenD = Direction.NORTH;
		setDirection(chosenD);
        
		while (!isOverBaggle()) {
			while (!isFacingWall()) {
				forward();
			}
			turnLeft();
            
			do {
				keepHandOnSideWall();
			} while (!(angleSum == 0 && isChosenDirectionFree()) && !isOverBaggle());
		}
		
		pickUpBaggle();
	}
    
	int angleSum = 0;
	Direction chosenD;
	Direction memorizedD;
    
	private boolean isChosenDirectionFree() {
		memorizedD = getDirection();
		setDirection(chosenD);
		if (!isFacingWall()) {
			return true;
		} else {
			setDirection(memorizedD);
			return false;
    }
	}
    
	public void keepHandOnSideWall() {
		keepHandOnRightWall();
	}
	
	private void keepHandOnRightWall() {
		turnRight();
		if (!isFacingWall()) {
			angleSum = angleSum + 1;
			forward(); // turn right then forward
		} else {
			turnLeft();
			if (!isFacingWall()) {
				forward(); // forward, direction did not change
			} else {
				turnLeft();
				if (!isFacingWall()) {
					angleSum = angleSum - 1;
					forward(); // turn left then forward
				} else {
					angleSum = -2;
					turnLeft(); // turn back then forward
					forward();
            }
        }
    }
	}
    
# END TEMPLATE
