package lessons.welcome.methods.doghouse;

import jlm.universe.bugglequest.SimpleBuggle;

public class MethodsDogHouseEntity extends SimpleBuggle {

	private int line = -1;

	@Override
	public void right() {
		throw new RuntimeException("right() forbidden in this exercise.");
	}

	@Override
	public void left() {
		for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
			if (s.getMethodName().contains("left")) {
				if (line != -1 && line != s.getLineNumber()) {
					System.out.println("Forbiden to use left() more than once in this exercise.");
					throw new RuntimeException("Forbiden to use left() more than once in this exercise.");
				} else {
					line = s.getLineNumber();
					super.left();
				}
			}

		}
	}
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	void dogHouse() {
		for (int i=0;i<4;i++) {
			forward();
			forward();
			left();
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */

	@Override
	public void run() {
		brushDown();
		dogHouse();
		brushUp();

		forward(4);

		brushDown();
		dogHouse();		
		brushUp();

		forward(2);
		left();
		forward(4);

		brushDown();
		dogHouse();		
		brushUp();

		forward(2);
		left();
		forward(4);

		brushDown();
		dogHouse();		
	} 
}
