package lessons.maze.wallfollower;

import jlm.core.model.Game;
import jlm.universe.Direction;

class ScalaWallFindFollowMazeEntity extends jlm.universe.bugglequest.SimpleBuggle {
	val uselessVariableExistingJustToMakeSureThatEclipseWontRemoveTheImport:Direction=null; /* If removed, user code can't use directions easily */
	override def setX(i: Int)  {
		if (isInited)
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setX() in this exercise."));
	}
	override def setY(i: Int)  { 
		if (isInited)
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setY() in this exercise."));
	}
	override def setPos(x: Int, y:Int)  { 
		if (isInited)
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setPos() in this exercise."));
	}
	 
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	def stepHandOnWall() {
		// PRE: we have a wall on the left
		// POST: we still have the same wall on the left, are one step ahead

		while (!isFacingWall()) {
			forward();
			left(); // change to right to get a right follower
		}
		right(); // change to left to get a right follower
	}

	def run() {
		// Make sure we have a wall to the left
		left();
		while (!isFacingWall())
			forward();
		right();

		while (!isOverBaggle())
			stepHandOnWall();

		pickupBaggle();
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}

