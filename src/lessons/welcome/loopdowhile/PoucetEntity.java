package lessons.welcome.loopdowhile;

import java.awt.Color;

import plm.core.model.Game;

public class PoucetEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument."));
	}
	@Override
	public void backward(int i) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument."));
	}
	
	public boolean crossing() {
		return getX()%5== 1 && getY()%5==1;
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
