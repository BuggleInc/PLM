package lessons.recursion.lego.tree;

import plm.universe.turtles.Turtle
import java.awt.Color
import plm.core.model.Game

class TreeEntity extends Turtle {

	override def setX(i: Int): Unit = {
		if (isInited)
			throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead."));
	}
	override def setY(i: Int): Unit = { 
		if (isInited)
			throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead."));
	}
	override def setPos(x: Int, y:Int): Unit = { 
		if (isInited)
			throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead."));
	}
	
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
