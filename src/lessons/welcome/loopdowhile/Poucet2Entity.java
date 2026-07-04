package lessons.welcome.loopdowhile;

import java.awt.Color;

import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Primitive;
import plm.core.model.Game;
import plm.universe.GridWorld;
import plm.universe.bugglequest.BuggleWorldCell;

@EntityPrimitives(PoucetEntityPrimitives.class)
public class Poucet2Entity extends plm.universe.bugglequest.SimpleBuggle implements PoucetEntityPrimitives {
	@Override
	public void forward(int i)  {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	@Override
	public void backward(int i) {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}
	
	// Compute the amount of free ways from the current cell
	@Override
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
	@Override
	public boolean exitReached() {
		return getGroundColor()== Color.orange;
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
				stepForward();
				if (isOverBaggle())
					seen++;
			} while (! crossing());
			
			if (seen>2)
				left();
			else
				right();
		}
		stepForward();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
