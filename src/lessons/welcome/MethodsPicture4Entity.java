package lessons.welcome;

import java.awt.Color;

/* The suppress warning is sometimes mandatory for student code to compile cleanly */
@SuppressWarnings("unused")
public class MethodsPicture4Entity extends jlm.universe.bugglequest.SimpleBuggle {

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

	void squareA(Color c) {
		setBrushColor(c);
		
		forward();
		mark();

		turnLeft();
		forward();

		turnLeft();
		forward();
		mark();

		turnLeft();
		forward();
		turnLeft();
	}
	void squareB(Color c) {
		setBrushColor(c);
		mark();

		forward();

		turnLeft();
		forward();
		mark();

		turnLeft();
		forward();

		turnLeft();
		forward();
		turnLeft();
	}

	void bigSquare() {
		squareA(Color.red); 
		forward(2);
		squareB(Color.blue);
		backward(2);
		turnLeft();
		forward(2);
		turnRight();
		squareB(Color.yellow);
		forward(2);
		squareA(Color.green);
		
		backward(2);
		turnLeft();
		backward(2);
		turnRight();
	}
	
	public void run() {
		bigSquare();
		forward(4);
		bigSquare();
		
		backward(4);
		turnLeft();
		forward(4);
		turnRight();
		
		bigSquare(); 
		forward(4);
		bigSquare();
	}
	/* END TEMPLATE */
}
