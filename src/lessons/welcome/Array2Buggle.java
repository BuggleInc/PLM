package lessons.welcome;

import java.awt.Color;

public class Array2Buggle extends bugglequest.core.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException("Pas le droit d'utiliser setX(int) dans cet exercice");
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException("Pas le droit d'utiliser setY(int) dans cet exercice");
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException("Pas le droit d'utiliser setPos(int,int) dans cet exercice");
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
