package lessons.maze.randommouse;

import plm.core.model.Game;

class ScalaRandomMouseMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
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
	def run() {
		// Your code here 
		/* BEGIN SOLUTION */ 
		while (!isOverBaggle()) {
			random3() match { 
			     case 0 if (!isFacingWall()) => forward();
			     case 1                      => left();
			     case 2                      => right();
			     case _ =>
			}
		}
		pickupBaggle();
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	def random3():Int = {
		Math.random() match {
		  case n if (n<0.33) => return 0;
		  case n if (n<0.66) => return 1;
		  case _             => return 2;
		}
	}
}
