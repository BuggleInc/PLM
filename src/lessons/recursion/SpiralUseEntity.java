package lessons.recursion;


public class SpiralUseEntity extends universe.turtles.Turtle {

	public void spiral(int steps, int angle, int length, int increment)	{
	     if (steps <= 0) {
	          // do nothing
	     } else {
	          forward(length);
	          turnLeft(angle);
	          spiral(steps-1, angle, length+increment, increment);
	     }
	}
	
	/* BEGIN TEMPLATE */
	void doit(int page) {
		/* BEGIN SOLUTION */
		switch (page) {
		case 0:	spiral(100,91,1,2);  break;
		case 1:	spiral(100,121,1,2); break;
		case 2: spiral(5,72,100,0);  break;
		case 3: spiral(5,144,150,0); break;
		case 4: spiral(360,1,1,0);   break;	
		}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
	
	public void run() {
		doit((Integer)getParam(0));
	}
}
