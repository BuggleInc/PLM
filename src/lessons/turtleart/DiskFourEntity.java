package lessons.turtleart;

import java.awt.Color;

import plm.universe.turtles.Turtle;

public class DiskFourEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		quadrant();
		setColor(Color.RED);
		quadrant();
		setColor(Color.ORANGE);
		quadrant();
		setColor(Color.MAGENTA);
		quadrant();
	}
	public void quadrant() {
		for (int i=0; i<90;i++) {
			forward(100);
			backward(100);
			right(1);
		}		
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
