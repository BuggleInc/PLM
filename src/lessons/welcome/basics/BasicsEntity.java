package lessons.welcome.basics;

import jlm.universe.bugglequest.SimpleBuggle;

public class BasicsEntity extends SimpleBuggle {

	@Override
	public void run() {
		/* BEGIN TEMPLATE */
		/* BEGIN SOLUTION */
		brushDown();
		for (int i=0;i<4;i++) {
			forward(2);
			right();
		}
		brushUp();
		forward();
		right();
		forward();
		left();
		/* END SOLUTION */
		/* END TEMPLATE */
	}

}
