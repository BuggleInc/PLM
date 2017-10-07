package lessons.welcome.loopfor;

import plm.universe.bugglequest.SimpleBuggle

import java.util.Locale
import plm.core.model.I18nManager
import org.xnap.commons.i18n.I18n

class ScalaLoopForEntity extends SimpleBuggle {
	override def forward(i: Int)  {
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
		var cpt = 0
		while (!isOverBaggle()) {
			cpt+=1;
			forward();
		}
		pickupBaggle();
		for (cpt2 <- 0  to cpt-1) {
			backward();
		}
		dropBaggle();
		/* END SOLUTION */
	}
}
