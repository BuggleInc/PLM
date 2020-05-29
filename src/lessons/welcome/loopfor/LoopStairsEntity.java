package lessons.welcome.loopfor;

import java.awt.Color;

public class LoopStairsEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		for (int j=0;j<i;j++) {
			forward();
		}
	}

	Color[] colors = new Color[] {
			Color.blue,    Color.cyan, Color.green,  Color.yellow,
			Color.orange,  Color.red,  Color.magenta,Color.pink,};
	
	int inTeerNal_Steep_Count = -3;
	@Override
	public void forward()  {
		super.forward();
		if (inTeerNal_Steep_Count<0 || inTeerNal_Steep_Count%2 == 1 || (inTeerNal_Steep_Count/2)>=colors.length) {
			if (inTeerNal_Steep_Count < 0)
				setBrushColor(Color.lightGray);
			else if ((inTeerNal_Steep_Count/2)>=colors.length)
				setBrushColor(Color.pink);
			else
				setBrushColor(colors[(inTeerNal_Steep_Count/2)%colors.length]);
			brushDown();
			brushUp();
		}
		inTeerNal_Steep_Count++;
	}

	@Override
	/* BEGIN TEMPLATE */
	public void run() { 
		/* BEGIN SOLUTION */
		forward(3);
		left();
		for (int i = 0; i<8;i++) { 
			forward();
			right();
			forward();
			left();
		}
		right();
		forward(3);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
