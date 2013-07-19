package lessons.welcome.loop.forloop;

import java.awt.Color;

public class LoopStairsEntity extends jlm.universe.bugglequest.SimpleBuggle {
	Color[] colors = new Color[] {
			Color.blue,    Color.cyan, Color.green,  Color.yellow,
			Color.orange,  Color.red,  Color.magenta,Color.pink,};
	@Override
	public void forward(int i)  { 
		throw new RuntimeException("forward(int) forbidden in this exercise");
	}
	
	int step = -3;
	@Override
	public void forward()  {
		super.forward();
		if (step<0 || step%2 == 1 || (step/2)>=colors.length) {
			if (step < 0)
				setBrushColor(Color.lightGray);
			else if ((step/2)>=colors.length)
				setBrushColor(Color.pink);
			else
				setBrushColor(colors[(step/2)%colors.length]);
			brushDown();
			brushUp();
		}
		step++;
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException("backward(int) forbidden in this exercise");
	}
	
	@Override
	/* BEGIN TEMPLATE */
	public void run() { 
		forward();
		forward();
		forward();
		turnLeft();
		/* BEGIN SOLUTION */
		for (int i = 0; i<8;i++) { 
			forward();
			turnRight();
			forward();
			turnLeft();
		}
		turnRight();
		forward();
		forward();
		forward();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
