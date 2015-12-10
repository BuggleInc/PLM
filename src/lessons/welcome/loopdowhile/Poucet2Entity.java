package lessons.welcome.loopdowhile;

import java.awt.Color;

import plm.universe.GridWorld;
import plm.universe.bugglequest.BuggleWorldCell;

public class Poucet2Entity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	@Override
	public void backward(int i) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}
	
	// Compute the amount of free ways from the current cell
	public boolean crossing() {
		BuggleWorldCell here = (BuggleWorldCell) ((GridWorld) world).getCell(getX(),getY());
		BuggleWorldCell right = (BuggleWorldCell) ((GridWorld) world).getCell( (getX()+1)% ((GridWorld) world).getWidth() , getY());
		BuggleWorldCell below = (BuggleWorldCell) ((GridWorld) world).getCell(getX() , (getY()+1)% ((GridWorld) world).getHeight());
		
		int open = 0;
		if (!here.hasLeftWall())
			open++;
		if (!here.hasTopWall())
			open++;
		if (!right.hasLeftWall())
			open++;
		if (!below.hasTopWall())
			open++;
		
		return open>2 || (here.hasLeftWall() != right.hasLeftWall()) || (here.hasTopWall() != below.hasTopWall());
	}
	public boolean exitReached() {
		return getGroundColor().equals(Color.orange);
	}	
	/* BINDINGS TRANSLATION */
	boolean sortieTrouvee() { return exitReached(); }
	boolean croisement() { return crossing(); }

	

	@Override
	/* BEGIN TEMPLATE */
	public void run() { 
		/* BEGIN SOLUTION */
		while (!exitReached()) {
			int seen = 0;
			
			do {
				forward();
				if (isOverBaggle())
					seen++;
			} while (! crossing());
			
			if (seen>2)
				left();
			else
				right();
		}
		forward();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
