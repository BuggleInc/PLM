package lessons.maze.randommouse;


class RandomMouseMazeEntity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */ 
	override def run(): Unit = {
		// Your code here 
		/* BEGIN SOLUTION */ 
		def random3():Int = {
			Math.random() match {
				case n if (n<0.33) => return 0;
				case n if (n<0.66) => return 1;
				case _             => return 2;
			}
		}
		
		while (!isOverBaggle()) {
			random3() match { 
			     case 0 if (!isFacingWall()) => stepForward();
			     case 1                      => left();
			     case 2                      => right();
			     case _ =>
			}
		}
		pickupBaggle();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	/* END REMOTE */
}
