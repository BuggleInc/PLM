package lessons.recursion.lego.tree;

import plm.universe.turtles.Turtle
import java.awt.Color

class TreeEntity extends Turtle {

	
	/* BEGIN REMOTE */
	/* BEGIN TEMPLATE */
	def tree(steps:Int, length:Double, angle:Double, shrink:Double): Unit = {
		/* BEGIN SOLUTION */
		if (steps != 0) {
		    current(steps)
			forward(length);
			right(angle);	         
			tree(steps-1, length*shrink, angle, shrink);
			left(2*angle);	         
			tree(steps-1, length*shrink, angle, shrink);
			right(angle);	         
			current(steps);
			backward(length);
		}
	}
	def subtree(steps:Int, length:Double, angle:Double, shrink:Double): Unit = {
		if (steps != 0) {
			setColor(Color.black)
			forward(length);
			right(angle);	         
			subtree(steps-1, length*shrink, angle, shrink);
			left(2*angle);	         
			subtree(steps-1, length*shrink, angle, shrink);
			right(angle);	         
			backward(length);
		}
		/* END SOLUTION */	
	}
	/* END TEMPLATE */

	override def run(): Unit = {
		tree(getParamInt(0),getParamDouble(1),
		    getParamDouble(2),getParamDouble(3));
	}
	/* END REMOTE */
}
