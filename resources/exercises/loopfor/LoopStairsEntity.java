package loopfor;

import java.awt.Color;
import java.util.Locale;

import org.xnap.commons.i18n.I18n;

import plm.core.model.I18nManager;

public class LoopStairsEntity extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		Locale locale = getWorld().getLocale();
		I18n i18n = I18nManager.getI18n(locale);
		throw new RuntimeException(i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	@Override
	public void backward(int i) {
		Locale locale = getWorld().getLocale();
		I18n i18n = I18nManager.getI18n(locale);
		throw new RuntimeException(i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

	Color[] colors = new Color[] {
			Color.blue,    Color.cyan, Color.green,  Color.yellow,
			Color.orange,  Color.red,  Color.magenta,Color.pink,};
	
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
	/* BEGIN TEMPLATE */
	public void run() { 
		/* BEGIN SOLUTION */
		forward();
		forward();
		forward();
		left();
		for (int i = 0; i<8;i++) { 
			forward();
			right();
			forward();
			left();
		}
		right();
		forward();
		forward();
		forward();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
