package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.universe.bugglequest.SimpleBuggle;

class ScalaPatternPictureEntity extends SimpleBuggle {

	/* BEGIN TEMPLATE */
	override def run() {
		/* BEGIN SOLUTION */
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
	def mark() {
		brushDown();
		brushUp();
	}

	def squareA(c:Color) {
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

	def squareB(c: Color) {
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

	def bigSquare() {
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
	/* END SOLUTION */
	}
	/* END TEMPLATE */
}
