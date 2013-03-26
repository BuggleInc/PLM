package lessons.welcome;

import java.awt.Color;

public class Array2Entity extends jlm.universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException("setX(int) forbidden in this exercise");
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException("setY(int) forbidden in this exercise");
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException("setPos(int,int) forbidden in this exercise");
	}

	/* BEGIN SOLUTION */
	void mark(Color c){
		setBrushColor(c);
		brushDown();
		brushUp();
	}
	
	public void run() {
		Color[] colors = new Color[getWorldHeight()];
		
		/* read the colors */
		colors[0]=getGroundColor();
		for (int i=1;i<getWorldHeight();i++) {
			forward();
			colors[i]=getGroundColor();
		}
		backward(getWorldHeight()-1);
		
		/* Duplicate the pattern */
		for (int i=1; i<getWorldWidth();i++) {
			turnLeft();
			forward();
			turnRight();
			makeLine(colors);
		}
	}
	
	void makeLine(Color[] colors) {
		int offset = Integer.parseInt(readMessage());
		mark(colors[(0+offset)%colors.length]);
		for (int i=1;i<getWorldWidth();i++) {
			forward();
			mark(colors[(i+offset)%colors.length]);
		}
		backward(getWorldHeight()-1);
	}
	/* END TEMPLATE */
}
