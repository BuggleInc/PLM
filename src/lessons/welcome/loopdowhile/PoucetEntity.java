package lessons.welcome.loopdowhile;

import java.awt.Color;

public class PoucetEntity extends jlm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException("forward(int) forbidden in this exercise");
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException("backward(int) forbidden in this exercise");
	}

	public boolean crossing() {
		return getX()%5== 1 && getY()%5==1;
	}
	public boolean exitReached() {
		return getGroundColor().equals(Color.orange);
	}

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
