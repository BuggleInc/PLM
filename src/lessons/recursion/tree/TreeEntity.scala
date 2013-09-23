package lessons.recursion.tree;

import plm.universe.turtles.Turtle;
import java.awt.Color

class ScalaTreeEntity extends Turtle {

	val colors:Array[Color] = Array(Color.cyan,      Color.blue,   Color.magenta, 
									Color.orange,    Color.yellow, Color.green,
									Color.lightGray, Color.gray,   Color.darkGray,   Color.black, Color.red)
									
	def current(v:Int) {
	  if (v>=colors.length)
	    setColor(colors(colors.length -1));
	  setColor(colors(v))
	}
  
	/* BEGIN TEMPLATE */
	def tree(steps:Int, length:Double, angle:Double, shrink:Double)	{
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
		/* END SOLUTION */	
	}
	/* END TEMPLATE */
	def subtree(steps:Int, length:Double, angle:Double, shrink:Double)	{
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

	override def run() {
		tree(getParam(0).asInstanceOf[Int],getParam(1).asInstanceOf[Double],
		    getParam(2).asInstanceOf[Double],getParam(3).asInstanceOf[Double]);
	}
}
