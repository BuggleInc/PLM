package lessons.traversal;

import universe.bugglequest.SimpleBuggle;

public class LineByLineBuggle extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	
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
	}
	
	
	@Override
	public void run() {
		int cpt=0;
		do {
			writeMessage(cpt);
			nextStep();
			cpt++;
		} while (!endingPosition());
		writeMessage(cpt);
	}

	/* END TEMPLATE */	
}
