package lessons.welcome.loopfor;

import java.awt.Color;

public class LoopCourseEntity extends jlm.universe.bugglequest.SimpleBuggle {
	Color[] colors = new Color[] {
			Color.white,
			new Color(255,240,240),new Color(255,220,220),new Color(255,205,205),
			new Color(255,190,190),new Color(255,170,170),new Color(255,150,150),
			new Color(255,130,130),new Color(255,110,110),new Color(255,45,45),
			new Color(255,5,5)};
	@Override
	public void forward(int i)  { 
		throw new RuntimeException("forward(int) forbidden in this exercise");
	}
	@Override
	public void forward()  {
		super.forward();
		Color c = getGroundColor();
		for (int i=0;i<colors.length-1;i++)
			if (colors[i].equals(c)) {
				c = colors[i+1];
				break;
			}
		setBrushColor(c);
		brushDown();
		brushUp();
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException("backward(int) forbidden in this exercise");
	}
	


	@Override
	/* BEGIN TEMPLATE */
	public void run() { 
		/* BEGIN SOLUTION */
		for (int i = 0; i<10;i++) 
			for (int side=0;side<4;side++){
				for (int step=0;step<8;step++)
					forward();
				left();
			}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
