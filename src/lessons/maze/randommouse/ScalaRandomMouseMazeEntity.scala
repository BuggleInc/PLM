package lessons.maze.randommouse;

import plm.core.model.Game;

class ScalaRandomMouseMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def setX(i: Int)  {
		if (isInited)
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead."));
	}
	override def setY(i: Int)  { 
		if (isInited)
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead."));
	}
	override def setPos(x: Int, y:Int)  { 
		if (isInited)
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead."));
	}

	/* BEGIN TEMPLATE */ 
	override def run() {
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
