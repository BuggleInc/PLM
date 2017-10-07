package lessons.welcome.variables;

import java.util.Locale
import plm.core.model.I18nManager
import org.xnap.commons.i18n.I18n

class ScalaRunFourEntity extends plm.universe.bugglequest.SimpleBuggle {
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
		var cpt = 0;
		while (cpt != 4) {
			forward();
			if (isOverBaggle())
				cpt += 1
		}
		/* END SOLUTION */
	}
}
