package lessons.recursion.spiral;

import jlm.universe.turtles.Turtle;

class ScalaSpiralUseEntity extends Turtle {

	def spiral(steps:Int, angle:Int, length:Int, increment:Int)	{
		if (steps <= 0) {
			return;
		} else {
			forward(length);
			left(angle);
			spiral(steps-1, angle, length+increment, increment);
		}
	}

	/* BEGIN TEMPLATE */
	def doit(page:Int) {
		/* BEGIN SOLUTION */
		page match {
		case 0 =>	spiral(100,90+1,1,2);
		case 1 =>	spiral(100,120+1,1,2);
		case 2 => spiral(5,360/5,100,0); 
		case 3 => spiral(5,2*360/5,150,0);
		case 4 => spiral(360,1,1,0);   	
		case _ => 
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	override def run() {
		doit(getParam(0).asInstanceOf[Int]);
	}
}
