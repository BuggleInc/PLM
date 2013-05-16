package lessons.welcome.methods.picture;

import java.awt.Color;

import jlm.universe.bugglequest.SimpleBuggle;
/* The suppress warning is sometimes mandatory for student code to compile cleanly */
@SuppressWarnings("unused")
public class MethodsPictureEntity extends SimpleBuggle {

	/* BEGIN TEMPLATE */
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
	/* END SOLUTION */
	/* END TEMPLATE */
}
