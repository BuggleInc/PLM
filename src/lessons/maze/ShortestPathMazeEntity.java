package lessons.maze;

import jlm.universe.Direction;
import jlm.universe.bugglequest.BuggleWorld;
import jlm.universe.bugglequest.BuggleWorldCell;

public class ShortestPathMazeEntity extends jlm.universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException("setX(int) forbidden in this exercise");
		super.setX(i);
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException("setY(int) forbidden in this exercise");
		super.setY(i);
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException("setPos(int,int) forbidden in this exercise");
		super.setPos(i,j);
	}

	public BuggleWorld getMyWorld() {
		return (BuggleWorld) getWorld();
	}

	/* BEGIN SOLUTION */
	// tools functions
	public BuggleWorldCell bottomCell(int x, int y) {
	    return getMyWorld().getCell(x, (y + 1) % getWorldHeight());
	}

	public BuggleWorldCell rightCell(int x, int y) {
	    return getMyWorld().getCell((x + 1) % getWorldWidth(), y); 
	}  

	public BuggleWorldCell topCell(int x, int y) {
	    return getMyWorld().getCell(x, ( y + getWorldHeight() - 1) % getWorldHeight());
	}

	public BuggleWorldCell leftCell(int x, int y) {
	    return getMyWorld().getCell((x + getWorldWidth() - 1) % getWorldWidth(), y); 
	}  

	public boolean hasTopWall(int x, int y) {
	    return getMyWorld().getCell(x, y).hasTopWall();
	}

	public boolean hasLeftWall(int x, int y) {
	    return getMyWorld().getCell(x, y).hasLeftWall(); 
	}  

	public boolean hasRightWall(int x, int y) {
	    return getMyWorld().getCell((x + 1) % getWorldWidth(), y).hasLeftWall(); 
	}  

	public boolean hasBottomWall(int x, int y) {
	    return getMyWorld().getCell(x, (y + 1) % getWorldHeight()).hasTopWall();
	}


	public boolean setValueIfLess(int x, int y, int val) {
		BuggleWorldCell cell = getMyWorld().getCell(x,y); 

	    if (! cell.hasContent()) {
	      cell.setContentFromInt(val);
	      return true;
	    } else {
	      int currentVal = cell.getContentAsInt();
	      if (val < currentVal) {
	         cell.setContentFromInt(val); 
	         return true;  
	      }
	    }
	
		return false;
	}

	public void evaluatePaths() {
	 	// looking for labyrinth exit	
		for (int x = 0; x < getWorldWidth(); x++)
			for (int y = 0; y < getWorldHeight(); y++) {       
				BuggleWorldCell c = getMyWorld().getCell(x,y);
				if (c.hasBaggle())
					c.setContentFromInt(0);   
			}

		while (true) {   
			for (int x = 0; x < getWorldWidth(); x++) {
				for (int y = 0; y < getWorldHeight(); y++) {  
					BuggleWorldCell c = getMyWorld().getCell(x,y); 
					boolean changed = false;
					if (c.hasContent()) {
						int val = c.getContentAsInt();
						if (! hasBottomWall(x,y))
							changed |= setValueIfLess(x, (y + 1) % getWorldHeight(), val + 1);

						if (! hasRightWall(x,y))
							changed |= setValueIfLess((x + 1) % getWorldWidth(), y, val + 1);

						if (! hasTopWall(x,y))
							changed |= setValueIfLess(x, (y+getWorldHeight() - 1) % getWorldHeight(), val + 1);

						if (! hasLeftWall(x,y))
							changed |= setValueIfLess((x +getWorldWidth() - 1) % getWorldWidth(), y, val + 1);
	
						if (changed && x == getX() && y == getY())
							return ;
					}
	   			}    
	  		}
	 	}
	}

	public void followShortestPath() {
		while (! isOverBaggle()) {

			int zx = getX();
			int zy = getY();

			int topValue = Integer.MAX_VALUE;
			int bottomValue = Integer.MAX_VALUE;
			int leftValue = Integer.MAX_VALUE;
			int rightValue = Integer.MAX_VALUE;

			if (! hasTopWall(zx, zy))
				topValue = topCell(zx,zy).hasContent()?topCell(zx,zy).getContentAsInt():Integer.MAX_VALUE;

			if (! hasBottomWall(zx, zy))
				bottomValue = bottomCell(zx,zy).hasContent()?bottomCell(zx,zy).getContentAsInt():Integer.MAX_VALUE;

			if (! hasLeftWall(zx, zy))
				leftValue = leftCell(zx,zy).hasContent()?leftCell(zx,zy).getContentAsInt():Integer.MAX_VALUE;

			if (! hasRightWall(zx, zy))
				rightValue = rightCell(zx,zy).hasContent()?rightCell(zx,zy).getContentAsInt():Integer.MAX_VALUE;

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
	}
	/* END SOLUTION */

	public void run() {
	  evaluatePaths(); // write on each case the distance to the maze exit
	  followShortestPath(); // make the buggle follow the shortest path
	  pickUpBaggle(); // enjoy the baggle!           
	}
	/* END TEMPLATE */
}
