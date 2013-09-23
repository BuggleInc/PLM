package lessons.recursion.tree;

import java.awt.Color;

import plm.universe.turtles.Turtle;

public class TreeEntity extends Turtle {

	Color[] colors = new Color[] {Color.cyan,      Color.blue,   Color.magenta, 
			Color.orange,    Color.yellow, Color.green,
			Color.lightGray, Color.gray,   Color.darkGray,   Color.black, Color.red};

	private void current(int v) {
		if (v>=colors.length)
			setColor(colors[colors.length -1]);
		setColor(colors[v]);
	}


	/* BEGIN TEMPLATE */
	public void tree(int steps, double length, double angle, double shrink)	{
		/* BEGIN SOLUTION */
		if (steps <= 0) {
			/* do nothing */
		} else {
			current(steps);
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
	public void subtree(int steps, double length, double angle, double shrink)	{
		if (steps != 0) {
			setColor(Color.black);
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

	public void run() {
		tree((Integer)getParam(0),(Double)getParam(1),(Double)getParam(2),(Double)getParam(3));
	}
}
