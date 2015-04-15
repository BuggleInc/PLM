package lessons.welcome.array.basics;

import java.awt.Color;

import plm.universe.bugglequest.SimpleBuggle;

public class Array2Entity extends SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setX(x) in this exercise. Walk to your goal instead."));
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setY(y) in this exercise. Walk to your goal instead."));
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException(getGame().i18n.tr("Sorry Dave, I cannot let you use setPos(x,y) in this exercise. Walk to your goal instead."));
	}

	/* BEGIN TEMPLATE */
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
			left();
			forward();
			right();
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
	/* END SOLUTION */
	/* END TEMPLATE */
}
