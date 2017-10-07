package methods.args;

import java.util.Locale;

import org.xnap.commons.i18n.I18n;

import plm.core.model.I18nManager;
import plm.universe.Direction;
import plm.universe.bugglequest.SimpleBuggle;

public class MethodsArgsEntity extends SimpleBuggle {
	@Override
	public void forward(int i)  { 
		Locale locale = getWorld().getLocale();
		I18n i18n = I18nManager.getI18n(locale);
		throw new RuntimeException(i18n.tr("I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	@Override
	public void backward(int i) {
		Locale locale = getWorld().getLocale();
		I18n i18n = I18nManager.getI18n(locale);
		throw new RuntimeException(i18n.tr("I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}


	@Override
	public void run() { 
		move(getY(),getDirection().equals(Direction.NORTH)); 
	} 

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	public void move(int nbPas, boolean goForward) {
		if (goForward) {
			for (int i=0; i<nbPas; i++) 
				forward();
		} else {
			for (int i=0; i<nbPas; i++) 
				backward();
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
