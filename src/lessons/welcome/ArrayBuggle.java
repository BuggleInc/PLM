package lessons.welcome;

import java.awt.Color;

public class ArrayBuggle extends jlm.bugglequest.SimpleBuggle {
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
		for (int i=0;i<getWorldHeight();i++) {
			colors[i]=getGroundColor();
			forward();
		}
		
		/* duplicate the pattern */
		for (int i=1; i<getWorldWidth();i++) {
			turnLeft();
			forward();
			turnRight();
			forward();
			makeLine(colors);
		}
	}
	
	void makeLine(Color[] colors) {
		for (int i=0;i<getWorldWidth();i++) {
			mark(colors[i]);
			forward();
		}
	}
	/* END TEMPLATE */
}
