# BEGIN SOLUTION 
  public void turnRandomly() {
		switch (random2()) {
		case 0:
			turnLeft();
			break;
		case 1:
			turnRight();
			break;
    }
	}
    
	public void takeRandomDirection() {
		if (isFacingWall()) {
			turnRandomly();
		} else {
			switch (random3()) {
			case 0:
				turnLeft();
				break;
			case 1:
				forward();
				break;
			case 2:
				turnRight();
				break;
        }
    }
	}
    
	public boolean atAJunction() {
		boolean junction = false;
        
		// check left
		turnLeft();
		if (!isFacingWall()) {
			junction = true;
		}
		turnRight();
        
		// can we skip next check?
		if (junction) {
			return true;
		} else {
			// check right
			turnRight();
			if (!isFacingWall()) {
				junction = true;
			}
			turnLeft();
		}
		return junction;
	}
    
# END TEMPLATE
