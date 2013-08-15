package lessons.welcome.methods.picture;

import java.awt.Color;

import jlm.universe.bugglequest.SimpleBuggle;

public class MethodsPicture4Entity extends SimpleBuggle {

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	void mark() {
		brushDown();
		brushUp();
	}

	void squareA(Color c) {
		setBrushColor(c);

		forward();
		mark();

		left();
		forward();

		left();
		forward();
		mark();

		left();
		forward();
		left();
	}

	void squareB(Color c) {
		setBrushColor(c);
		mark();

		forward();

		left();
		forward();
		mark();

		left();
		forward();

		left();
		forward();
		left();
	}

	void bigSquare() {
		squareA(Color.RED); 
		forward(2);
		squareB(Color.BLUE);
		backward(2);
		left();
		forward(2);
		right();
		squareB(Color.YELLOW);
		forward(2);
		squareA(Color.GREEN);

		backward(2);
		left();
		backward(2);
		right();
	}

	public void run() {
		bigSquare();
		forward(4);
		bigSquare();

		backward(4);
		left();
		forward(4);
		right();

		bigSquare(); 
		forward(4);
		bigSquare();
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
