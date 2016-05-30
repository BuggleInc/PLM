package lessons.welcome.array.basics;

import java.awt.Color;

public class Array1Entity extends plm.universe.bugglequest.SimpleBuggle {
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
	public void run() {
		/* BEGIN SOLUTION */
		Color[] colors = new Color[getWorldHeight()];

		/* read the colors */
		for (int i=0;i<getWorldHeight();i++) {
			colors[i]=getGroundColor();
			forward();
		}

		/* duplicate the pattern */
		for (int i=1; i<getWorldWidth();i++) {
			left();
			forward();
			right();
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
	void mark(Color c){
		setBrushColor(c);
		brushDown();
		brushUp();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
