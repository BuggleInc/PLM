package lessons.welcome;

public class MethodsDogHouseEntity extends universe.bugglequest.SimpleBuggle {

	private int line = -1;
	
	@Override
	public void turnRight() {
		throw new RuntimeException("Pas le droit d'utiliser turnRight dans cet exercice.");
	}
	
	@Override
	public void turnLeft() {
		for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
			if (s.getMethodName().contains("turnLeft")) {
				if (line != -1 && line != s.getLineNumber()) {
					System.out.println("Pas le droit d'utiliser plusieurs fois turnLeft() dans cet exercice.");
					throw new RuntimeException("Pas le droit d'utiliser plusieurs fois turnLeft() dans cet exercice.");
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
