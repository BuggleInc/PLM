package lessons.maze.wallfindfollow;

class WallFindFollowMazeEntity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		// Make sure we have a wall to the left
		left();
		while (!isFacingWall())
			step();
		right();

		while (!isOverBaggle())
			stepHandOnWall();

		pickupBaggle();
	}
  
	def stepHandOnWall(): Unit = {
		// PRE: we have a wall on the left
		// POST: we still have the same wall on the left, are one step ahead

		while (!isFacingWall()) {
			step();
			left(); // change to right to get a right follower
		}
		right(); // change to left to get a right follower
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}

