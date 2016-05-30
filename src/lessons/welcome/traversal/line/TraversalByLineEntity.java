package lessons.welcome.traversal.line;

import plm.universe.bugglequest.SimpleBuggle;

public class TraversalByLineEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		int cpt=0;
		do {
			writeMessage(cpt);
			nextStep();
			cpt++;
		} while (!endingPosition());
		writeMessage(cpt);
	}

	public void nextStep() {
		int x=getX();
		int y=getY();
		if (x < getWorldWidth()-1) {
			x++;
		} else {
			x = 0; 
			if (y < getWorldHeight()-1) {
				y++; 
			} else {
				y = 0;
			}
		}
		setPos(x,y);
	}

	public boolean endingPosition() {
		return (getX() == getWorldWidth()-1) && (getY() == getWorldHeight()-1);
		/* END SOLUTION */
	}
	/* END TEMPLATE */	

	@Override
	public void forward(int i)  { 
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward() in this exercise. Use setPos(x,y) instead."));
	}
	@Override
	public void forward()  {
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use forward() in this exercise. Use setPos(x,y) instead."));
	}
	@Override
	public void backward(int i) {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward() in this exercise. Use setPos(x,y) instead."));
	}
	@Override
	public void backward() {
		throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use backward() in this exercise. Use setPos(x,y) instead."));
	}
}
