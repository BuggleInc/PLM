package lessons.recursion;


public class SpiralUseEntity extends jlm.universe.turtles.Turtle {

	public void spiral(int steps, int angle, int length, int increment)	{
	     if (steps <= 0) {
	    	 return;
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
		case 0:	spiral(100,90+1,1,2);  break;
		case 1:	spiral(100,120+1,1,2); break;
		case 2: spiral(5,360/5,100,0);  break;
		case 3: spiral(5,2*360/5,150,0); break;
		case 4: spiral(360,1,1,0);   break;	
		}
		/* END SOLUTION */
}
	/* END TEMPLATE */
	
	public void run() {
		doit((Integer)getParam(0));
	}
}
