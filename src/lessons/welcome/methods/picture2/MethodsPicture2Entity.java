package lessons.welcome.methods.picture2;

import java.awt.Color;

/* The suppress warning is sometimes mandatory for student code to compile cleanly */
@SuppressWarnings("unused")
public class MethodsPicture2Entity extends jlm.universe.bugglequest.SimpleBuggle {

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

	void makePattern() {
		makeV(Color.YELLOW);
		makeV(Color.RED);
		makeV(Color.BLUE);
		makeV(Color.GREEN);
		forward(5);
	}

	void makeLine(int count){
		for (int i=0; i<count;i++)
			makePattern();
		backward(count*5);
	}

	void nextLine() {
		turnLeft();
		forward(5);
		turnRight();	
	}

	public void run() {
		for (int i=0; i<3;i++) {
			makeLine(3);
			nextLine();
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
