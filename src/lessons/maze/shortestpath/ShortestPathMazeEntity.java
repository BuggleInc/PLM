package lessons.maze.shortestpath;

import java.io.IOException;

import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.BuggleWorldCell;

public class ShortestPathMazeEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead."));
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead."));
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead."));
	}

	void setIndication(int x, int y, int i) {
		BuggleWorldCell c = ((BuggleWorld) world).getCell(x,y);
		generateOperationsChangeCellContent(c, c.getContent(), i+"", c.hasContent(), true);
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
	
	@Override
	public void command(String command, java.io.BufferedWriter out) {
		int num = Integer.parseInt((String) command.subSequence(0, 3));
		int nb,nb2,nb3;
		try{
		switch(num){
		case 143:
			nb = Integer.parseInt((command.split(" ")[1]));
			nb2 = Integer.parseInt((command.split(" ")[2]));
			nb3 = Integer.parseInt((command.split(" ")[3]));
			setIndication(nb,nb2,nb3);
			break;
		case 144:
			nb = Integer.parseInt((command.split(" ")[1]));
			nb2 = Integer.parseInt((command.split(" ")[2]));
			out.write(Integer.toString(getIndication(nb,nb2)));
			out.write("\n");
			out.flush();
			break;
		case 145:
			nb = Integer.parseInt((command.split(" ")[1]));
			nb2 = Integer.parseInt((command.split(" ")[2]));
			out.write(hasBaggle(nb,nb2)?"1":"0");
			out.write("\n");
			out.flush();
			break;
		case 146:
			nb = Integer.parseInt((command.split(" ")[1]));
			nb2 = Integer.parseInt((command.split(" ")[2]));
			out.write(hasTopWall(nb,nb2)?"1":"0");
			out.write("\n");
			out.flush();
			break;
		case 147:
			nb = Integer.parseInt((command.split(" ")[1]));
			nb2 = Integer.parseInt((command.split(" ")[2]));
			out.write(hasLeftWall(nb,nb2)?"1":"0");
			out.write("\n");
			out.flush();
			break;
		default:
			super.command(command, out);
			break;
			
		}
		}catch(IOException ioe){
			//TODO
			ioe.printStackTrace();
		}
		
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

		boolean changed = true;
		while (changed) {
			changed = false;
			for (int x = 0; x < getWorldWidth(); x++) {
				for (int y = 0; y < getWorldHeight(); y++) {  
					int indication = getIndication(x, y);
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
