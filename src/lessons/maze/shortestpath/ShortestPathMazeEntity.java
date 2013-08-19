package lessons.maze.shortestpath;

import jlm.core.model.Game;
import jlm.universe.Direction;
import jlm.universe.bugglequest.BuggleWorld;
import jlm.universe.bugglequest.BuggleWorldCell;

public class ShortestPathMazeEntity extends jlm.universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setX() in this exercise."));
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setY() in this exercise."));
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setPos() in this exercise."));
	}

	void setIndication(int x, int y, int i) {
		BuggleWorldCell c = ((BuggleWorld) world).getCell(x,y);
		c.setContent(""+i);
	}
	int getIndication(int x, int y) {
		BuggleWorldCell c = ((BuggleWorld) world).getCell(x,y);
		if (c.hasContent())
			return Integer.parseInt(c.getContent());
		return 9999;
	}
	boolean hasBaggle(int x, int y) {
		return ((BuggleWorld) world).getCell(x,y).hasBaggle();
	}
	boolean hasTopWall(int x, int y) {
		return ((BuggleWorld) world).getCell(x,y).hasTopWall();
	}
	boolean hasLeftWall(int x, int y) {
		return ((BuggleWorld) world).getCell(x,y).hasLeftWall();
	}

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		evaluatePaths(); // write on each case the distance to the maze exit
		followShortestPath(); // make the buggle follow the shortest path
		pickupBaggle(); // enjoy the baggle!           
	}
	// tools functions
	public boolean hasRightWall(int x, int y) {
		return hasLeftWall((x + 1) % getWorldWidth(), y); 
	}  

	public boolean hasBottomWall(int x, int y) {
		return hasTopWall(x, (y + 1) % getWorldHeight());
	}


	public boolean setValueIfLess(int x, int y, int val) {
		int existing = getIndication(x, y);
		if (val < existing) {
			setIndication(x, y, val);
			return true;
		}
		return false;
	}

	public void evaluatePaths() {
		// looking for labyrinth exit	
		for (int x = 0; x < getWorldWidth(); x++)
			for (int y = 0; y < getWorldHeight(); y++)        
				if (hasBaggle(x,y))
					setIndication(x, y, 0);

		while (true) {   
			for (int x = 0; x < getWorldWidth(); x++) {
				for (int y = 0; y < getWorldHeight(); y++) {  
					int indication = getIndication(x, y);
					boolean changed = false;
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
	}

	public void followShortestPath() {
		while (! isOverBaggle()) {

			int x = getX();
			int y = getY();

			int topValue = 9999;
			int bottomValue = 9999;
			int leftValue = 9999;
			int rightValue = 9999;

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
	boolean aBiscuit(int x, int y) { return hasBaggle(x,y); }
	boolean aMurNord(int x, int y) { return hasTopWall(x,y); }
	boolean aMurOuest(int x, int y){ return hasLeftWall(x, y); }
}
