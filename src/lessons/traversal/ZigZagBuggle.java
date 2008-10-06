package lessons.traversal;

import bugglequest.core.SimpleBuggle;

public class ZigZagBuggle extends SimpleBuggle {
	/* BEGIN TEMPLATE */		
	/* BEGIN SOLUTION */
	
	public void nextStep() {        
		int x=getX();
		int y=getY();

        if (y % 2 == 0) {
            if (x < getWorldWidth()-1) {
                x++;
            } else if (y < getWorldHeight()-1) {
                y++; 
            }
        } else {
            if (0 < x) {
                x--;
            } else if (y < getWorldHeight()-1) {
                y++; 
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
