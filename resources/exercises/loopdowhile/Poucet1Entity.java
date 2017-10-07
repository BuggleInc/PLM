package loopdowhile;

import java.awt.Color;
import java.util.Locale;

import org.xnap.commons.i18n.I18n;

import plm.core.model.I18nManager;

public class Poucet1Entity extends plm.universe.bugglequest.SimpleBuggle {
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
	
	public boolean crossing() {
		return getX()%5== 1 && getY()%5==1;
	}
	public boolean exitReached() {
		return getGroundColor().equals(Color.orange);
	}	
	/* BINDINGS TRANSLATION */
	boolean sortieTrouvee() { return exitReached(); }
	boolean croisement() { return crossing(); }

	

	@Override
	/* BEGIN TEMPLATE */
	public void run() { 
		/* BEGIN SOLUTION */
		while (!exitReached()) {
			int seen = 0;
			
			do {
				forward();
				if (isOverBaggle())
					seen++;
			} while (! crossing());
			
			if (seen>2)
				left();
			else
				right();
		}
		forward();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
