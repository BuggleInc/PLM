package lessons.welcome.loopfor;

import java.awt.Color;

import plm.core.model.Game;

public class LoopCourseForestEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use forward with an argument in this exercise."));
	}
	@Override
	public void backward(int i) {
		throw new RuntimeException(Game.i18n.tr("I'm sorry Dave, I'm affraid I can't let you use backward with an argument in this exercise."));
	}
	Color[] colors = new Color[] {
			new Color(0,155,0),
			new Color(50,155,0),
			new Color(100,155,0),
			new Color(140,155,0),
			new Color(160,155,0),
			new Color(180,155,0),
			new Color(200,155,0),
			new Color(210,155,0),
	};
	@Override
	public void forward()  {
		if (!haveSeenError())
			super.forward();
		Color c = getGroundColor();
		if (c.equals(Color.blue)) {
			if (!haveSeenError())
				javax.swing.JOptionPane.showMessageDialog(null, Game.i18n.tr("You fall into water."), Game.i18n.tr("Test failed"), javax.swing.JOptionPane.ERROR_MESSAGE);
			seenError();
		}
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
	/* BEGIN TEMPLATE */
	public void run() { 
		/* BEGIN SOLUTION */
		for (int i = 0; i<7;i++) 
			for (int side=0;side<4;side++){
				for (int step=0;step<4;step++)
					forward();
				left();
				for (int step=0;step<2;step++)
					forward();
				right();
				for (int step=0;step<4;step++)
					forward();
				right();
				forward();
				forward();
				left();
				for (int step=0;step<4;step++)
					forward();
				left();
			}
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
