package lessons.turtleart;

import plm.universe.turtles.Turtle;

/* Item 40 of http://billkerr2.blogspot.fr/2009/08/40-maths-shapes-challenges.html */

class ScalaKerr40Entity extends Turtle {

	/* BEGIN TEMPLATE */
	override def run():Unit= {
			/* BEGIN SOLUTION */
			val s=30;
			addSizeHint(150,140,150+s,140);
			for (i<-1 to 8) {
				forward(s);
				left(90);
				forward(s);
				right(45);
				forward(s);
				left(90);
				forward(s);
				right(90);
				forward(s);
				right(90);
				forward(s);
				left(45);
				forward(s);
				right(90);
				forward(s);
				left(45);
				forward(s);
				right(90);
				forward(s);
				left(45);
				forward(s);
				right(90);
				forward(s);
				right(90);
				forward(s);
				left(90);
				forward(s);
				right(45);
				forward(s);
				left(90);
				forward(s);
			}
			/* END SOLUTION */
	}
	/* END TEMPLATE */
}
