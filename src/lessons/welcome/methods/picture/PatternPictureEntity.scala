package lessons.welcome.methods.picture;

import java.awt.Color;

import plm.universe.bugglequest.SimpleBuggle;

class PatternPictureEntity extends SimpleBuggle {

	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	override def run(): Unit = {
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
	def mark(): Unit = {
		brushDown();
		brushUp();
	}

	def squareA(c:Color): Unit = {
		setBrushColor(c);

		stepForward();
		mark();

		left();
		stepForward();

		left();
		stepForward();
		mark();

		left();
		stepForward();
		left();
	}

	def squareB(c: Color): Unit = {
		setBrushColor(c);
		mark();

		stepForward();

		left();
		stepForward();
		mark();

		left();
		stepForward();

		left();
		stepForward();
		left();
	}

	def bigSquare(): Unit = {
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
	/* END REMOTE */
}
