package lessons.welcome;

import java.awt.Color;
import jlm.universe.Direction;

@SuppressWarnings("unused")
public class MethodsPictureEntity extends universe.bugglequest.SimpleBuggle {

	/* BEGIN SOLUTION */
	void mark() {
		brushDown();
		brushUp();
	}
		
	void makeV(Color c) {
		setBrushColor(c);
		forward();
		mark();
		
		forward();
		turnLeft();
		forward();
		mark();
		
		backward();
		turnRight();
		forward();
		mark();
		
		forward();
		turnLeft();
	}

	
	
	public void run() {
		makeV(Color.yellow);
		makeV(Color.red);
		makeV(Color.blue);
		makeV(Color.green);
	}
	/* END TEMPLATE */
}
