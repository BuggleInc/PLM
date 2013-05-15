# BEGIN SOLUTION 
  public void stepHandOnWall() {
	    // PRE: we have a wall on the left
	    // POST: we still have the same wall on the left, are one step ahead
            
            while (!isFacingWall()) {
                forward();
                turnLeft(); // change to turnRight to get a right follower
            }
            turnRight(); // change to turnLeft to get a right follower
	}
    
	public void run() {
	    // Make sure we have a wall to the left
	    turnLeft();
	    while (!isFacingWall())
            forward();
	    turnRight();
        
	    while (!isOverBaggle())
            stepHandOnWall();
        
	    pickUpBaggle();
	}
# END TEMPLATE
