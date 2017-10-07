package lessons.welcome.loopwhile;

import java.util.Locale;

import org.xnap.commons.i18n.I18n;

import plm.core.model.I18nManager;
import plm.universe.bugglequest.SimpleBuggle;

public class LoopWhileEntity extends SimpleBuggle {
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

	@Override
	public void run() {
		/* BEGIN TEMPLATE */
		/* BEGIN SOLUTION */
		while (!isFacingWall())
			forward();
		/* END SOLUTION */
		/* END TEMPLATE */
	}
}
