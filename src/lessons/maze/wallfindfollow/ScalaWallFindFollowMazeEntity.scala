package lessons.maze.wallfindfollow;

import plm.core.model.Game;
import plm.universe.Direction;

class ScalaWallFindFollowMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
	val uselessVariableExistingJustToMakeSureThatEclipseWontRemoveTheImport:Direction=null; /* If removed, user code can't use directions easily */
	override def setX(i: Int):Unit = {
		if (isInited)
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead."));
	}
	override def setY(i: Int):Unit = { 
		if (isInited)
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead."));
	}
	override def setPos(x: Int, y:Int):Unit = { 
		if (isInited)
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead."));
	}
	 
	/* BEGIN TEMPLATE */
	override def run():Unit = {
		/* BEGIN SOLUTION */
		// Make sure we have a wall to the left
		left();
		while (!isFacingWall())
			forward();
		right();

		while (!isOverBaggle())
			stepHandOnWall();

		pickupBaggle();
	}
  
	def stepHandOnWall():Unit = {
		// PRE: we have a wall on the left
		// POST: we still have the same wall on the left, are one step ahead

		while (!isFacingWall()) {
			forward();
			left(); // change to right to get a right follower
		}
		right(); // change to left to get a right follower
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}

