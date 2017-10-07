package methods.returning;

import java.util.Locale;

import org.xnap.commons.i18n.I18n;

import plm.core.model.I18nManager;

public class MethodsReturningCommonErr1 extends plm.universe.bugglequest.SimpleBuggle {
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
		for (int i=0; i<7; i++) {
			if (haveBaggle()) 
				return;
			right();
			forward();
			left();
		}
	}
	
	boolean sawBaggle = false;
	int count = 0;
	
	boolean haveBaggle() {
		while(!isOverBaggle()) {
			count++;
			forward();
			if(count>=6) {
				backward();backward();backward();backward();backward();backward();
				right();
				forward();
				left();
				count=0;
			}
		}	
		return true;
	}
}
