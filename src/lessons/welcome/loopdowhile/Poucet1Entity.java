package lessons.welcome.loopdowhile;

import java.awt.Color;

import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Primitive;
import plm.core.model.Game;

@EntityPrimitives(Poucet1Entity.class)
public class Poucet1Entity extends plm.universe.bugglequest.SimpleBuggle implements PoucetEntityPrimitives{
	@Override
	public void forward(int i)  {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	@Override
	public void backward(int i) {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

	public boolean crossing() {
		return getX()%5== 1 && getY()%5==1;
	}

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
