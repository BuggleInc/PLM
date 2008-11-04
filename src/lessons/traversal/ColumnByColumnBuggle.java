package lessons.traversal;

import universe.bugglequest.SimpleBuggle;

public class ColumnByColumnBuggle extends SimpleBuggle {
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */	
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
	}
	
	
	@Override
	public void run() {
		int cpt=0;
		writeMessage(cpt);
		while (!endingPosition()) {
			nextStep();
			cpt++;
			writeMessage(cpt);
		}
	}

	/* END TEMPLATE */	
}
