package lessons.maze.shortestpath;

import plm.universe.Direction
import plm.universe.bugglequest.BuggleWorld
import plm.universe.bugglequest.BuggleWorldCell;
import plm.core.model.Game

class ScalaShortestPathMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
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

	def setIndication(x:Int, y:Int, i:Int) {
		val c = world.asInstanceOf[BuggleWorld].getCell(x,y);
    generateOperationsChangeCellContent(c, c.getContent, i+"", c.hasContent, true);
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
	override def run() {
		// Your code here
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

		var changed = true;
		while (changed) { 
			changed = false
			for (x <- 0 to getWorldWidth() -1; y <- 0 to getWorldHeight()-1) {
				var indication = getIndication(x, y);
				if (indication != 9999) {
					if (! hasBottomWall(x,y))
						changed |= setValueIfLess(x, (y + 1) % getWorldHeight(), indication + 1);

					if (! hasRightWall(x,y))
						changed |= setValueIfLess((x + 1) % getWorldWidth(), y, indication + 1);

					if (! hasTopWall(x,y))
						changed |= setValueIfLess(x, (y+getWorldHeight() - 1) % getWorldHeight(), indication + 1);

					if (! hasLeftWall(x,y))
						changed |= setValueIfLess((x +getWorldWidth() - 1) % getWorldWidth(), y, indication + 1);

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
	
	/* BINDINGS TRANSLATION to French: Don't translate getIndication */
	def aBiscuit(x:Int, y:Int):Boolean = hasBaggle(x,y)
	def aMurNord(x:Int, y:Int):Boolean = hasTopWall(x,y)
	def aMurOuest(x:Int, y:Int):Boolean = hasLeftWall(x, y)
}
