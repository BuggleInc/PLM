package lessons.recursion.tree;

import jlm.universe.turtles.Turtle;

class ScalaTreeEntity extends Turtle {

	/* BEGIN TEMPLATE */
	def tree(steps:Int, length:Double, angle:Double, shrink:Double)	{
		/* BEGIN SOLUTION */
		if (steps <= 0) {
			/* do nothing */
		} else {
			forward(length);
			right(angle);	         
			tree(steps-1, length*shrink, angle, shrink);
			left(2*angle);	         
			tree(steps-1, length*shrink, angle, shrink);
			right(angle);	         
			backward(length);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	override def run() {
		tree(getParam(0).asInstanceOf[Int],getParam(1).asInstanceOf[Double],
		    getParam(2).asInstanceOf[Double],getParam(3).asInstanceOf[Double]);
	}
}
