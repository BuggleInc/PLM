package lessons.welcome.variables;

import java.util.Locale
import plm.core.model.I18nManager
import org.xnap.commons.i18n.I18n

class ScalaVariablesEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int) {
	  val locale: Locale = getWorld.getLocale
	  val i18n: I18n = I18nManager.getI18n(locale)
		throw new RuntimeException(i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}

	override def backward(i: Int) {
	  val locale: Locale = getWorld.getLocale
	  val i18n: I18n = I18nManager.getI18n(locale)
		throw new RuntimeException(i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}


	override def run() {
		/* BEGIN SOLUTION */
		var stepper = 0;
		while (!isOverBaggle()) {
			stepper += 1
			forward()
		}
		pickupBaggle();
		while (stepper>0) {
			backward()
			stepper -= 1
		}
		dropBaggle();
		/* END SOLUTION */
	}
}
