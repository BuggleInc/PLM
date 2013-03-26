package lessons.welcome;

public class MethodsDogHouseEntity extends jlm.universe.bugglequest.SimpleBuggle {

	private int line = -1;
	
	@Override
	public void turnRight() {
		throw new RuntimeException("turnRight() forbidden in this exercise.");
	}
	
	@Override
	public void turnLeft() {
		for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
			if (s.getMethodName().contains("turnLeft")) {
				if (line != -1 && line != s.getLineNumber()) {
					System.out.println("Forbiden to use turnLeft() more than once in this exercise.");
					throw new RuntimeException("Forbiden to use turnLeft() more than once in this exercise.");
				} else {
					line = s.getLineNumber();
					super.turnLeft();
				}
			}
				
		}
	}

	/* BEGIN SOLUTION */
    void dogHouse() {
		for (int i=0;i<4;i++) {
			forward();
			forward();
			turnLeft();
		}
	}
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
		turnLeft();
		forward(4);
		
		brushDown();
		dogHouse();		
		brushUp();
		
		forward(2);
		turnLeft();
		forward(4);
		
		brushDown();
		dogHouse();		
	} 
}
