package lessons.welcome.traversal.column;

import plm.universe.bugglequest.SimpleBuggle;

public class TraversalByColumnEntity extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */	
		int cpt=0;
		writeMessage(cpt);
		while (!endingPosition()) {
			nextStep();
			cpt++;
			writeMessage(cpt);
		}
	}
	public void nextStep() {	
		int x=getX();
		int y=getY();

		if (y < getWorldHeight()-1) {
			y++;
		} else {
			y = 0;
			if (x < getWorldWidth()-1) {
				x++;
			} else {
				x = 0; 
			}
		}
		setPos(x,y);
	}

	public boolean endingPosition() {
		return (getX() == getWorldWidth() -1) && (getY() == getWorldHeight()-1);
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
