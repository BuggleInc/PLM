package lessons.maze.shortestpath;

import plm.universe.Direction
import plm.universe.bugglequest.BuggleWorld
import plm.universe.bugglequest.BuggleWorldCell;
import plm.core.model.Game

class ScalaShortestPathMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
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

	def setIndication(x:Int, y:Int, i:Int) {
		val c = world.asInstanceOf[BuggleWorld].getCell(x,y);
		c.setContent(""+i);
	}
	def getIndication(x:Int, y:Int): Int =  {
		val c = world.asInstanceOf[BuggleWorld].getCell(x,y);
		if (c.hasContent())
			return c.getContent().toInt;
		return 9999;
	}
	def hasBaggle(x:Int, y:Int):   Boolean = world.asInstanceOf[BuggleWorld].getCell(x,y).hasBaggle();
	def hasTopWall(x:Int, y:Int):  Boolean = world.asInstanceOf[BuggleWorld].getCell(x,y).hasTopWall();
	def hasLeftWall(x:Int, y:Int): Boolean = world.asInstanceOf[BuggleWorld].getCell(x,y).hasLeftWall();

	/* BEGIN TEMPLATE */
	def run() {
		/* BEGIN SOLUTION */
		evaluatePaths(); // write on each case the distance to the maze exit
		followShortestPath(); // make the buggle follow the shortest path
		pickupBaggle(); // enjoy the biscuit           
	}
	// tools functions
	def hasRightWall(x:Int, y:Int):  Boolean = hasLeftWall((x + 1) % getWorldWidth(), y); 
	def hasBottomWall(x:Int, y:Int): Boolean = hasTopWall(x, (y + 1) % getWorldHeight());

	def setValueIfLess(x:Int, y:Int, i:Int): Boolean = {
		var existing = getIndication(x, y);
		if (i < existing) {
			setIndication(x, y, i);
			return true;
		}
		return false;
	}

	def evaluatePaths() {
		// looking for labyrinth exit	
		for (x <- 0 to getWorldWidth() -1; y <- 0 to getWorldHeight()-1)        
			if (hasBaggle(x,y))
				setIndication(x, y, 0);

		while (true) {   
			for (x <- 0 to getWorldWidth() -1; y <- 0 to getWorldHeight()-1) {
				var indication = getIndication(x, y);
				var changed = false;
				if (indication != 9999) {
					if (! hasBottomWall(x,y))
						changed |= setValueIfLess(x, (y + 1) % getWorldHeight(), indication + 1);

					if (! hasRightWall(x,y))
						changed |= setValueIfLess((x + 1) % getWorldWidth(), y, indication + 1);

					if (! hasTopWall(x,y))
						changed |= setValueIfLess(x, (y+getWorldHeight() - 1) % getWorldHeight(), indication + 1);

					if (! hasLeftWall(x,y))
						changed |= setValueIfLess((x +getWorldWidth() - 1) % getWorldWidth(), y, indication + 1);

					if (changed && x == getX() && y == getY())
						return ; // reached the buggle, that's enough
				}    
			}
		}
	}

	def followShortestPath() {
		while (! isOverBaggle()) {

			var x = getX();
			var y = getY();

			var topValue = 9999;
			var bottomValue = 9999;
			var leftValue = 9999;
			var rightValue = 9999;

			if (! hasTopWall(x, y))
				topValue = getIndication(x, (y + getWorldHeight() - 1) % getWorldHeight());

			if (! hasBottomWall(x, y))
				bottomValue = getIndication(x, (y+1) % getWorldHeight());

			if (! hasLeftWall(x, y))
				leftValue = getIndication((x +getWorldWidth() - 1) % getWorldWidth(), y);

			if (! hasRightWall(x, y))
				rightValue = getIndication((x + 1) % getWorldWidth(), y);
			
			if (topValue <= bottomValue && topValue <= leftValue && topValue <= rightValue)
				setDirection(Direction.NORTH);
			else if (rightValue <= topValue && rightValue <= bottomValue && rightValue <= leftValue)
				setDirection(Direction.EAST);
			else if (leftValue <= rightValue && leftValue <= bottomValue && leftValue <= topValue)
				setDirection(Direction.WEST);
			else if (bottomValue <= topValue && bottomValue <= rightValue && bottomValue <= leftValue)
				setDirection(Direction.SOUTH);

			forward();
		}    
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
