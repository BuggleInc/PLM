package lessons.welcome.array.basics;

import java.awt.Color;

import plm.core.model.Game;

public class Array1Entity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void setX(int i)  {
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setX() in this exercise."));
	}
	@Override
	public void setY(int i)  { 
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setY() in this exercise."));
	}
	@Override
	public void setPos(int i,int j)  { 
		if (isInited())
			throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use setPos() in this exercise."));
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
