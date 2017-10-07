package lessons.welcome.methods.basics;


import java.util.Locale
import plm.core.model.I18nManager
import org.xnap.commons.i18n.I18n

class ScalaMethodsEntity extends plm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int) {
	  val locale: Locale = getWorld.getLocale
	  val i18n: I18n = I18nManager.getI18n(locale)
		throw new RuntimeException(i18n.tr("I cannot let you use forward with an argument. Use a loop instead."));
	}
	override def backward(i: Int) {
	  val locale: Locale = getWorld.getLocale
	  val i18n: I18n = I18nManager.getI18n(locale)
		throw new RuntimeException(i18n.tr("I cannot let you use backward with an argument. Use a loop instead."));
	}

	override def run() { 
		/* BEGIN TEMPLATE */
		def goAndGet() {
			/* BEGIN SOLUTION */
			var i = 0;
			while (!isOverBaggle()) {
				i += 1;
				forward();
			}
			pickupBaggle();
			while (i>0) {
				backward();
				i -= 1;
			}
			dropBaggle();
			/* END SOLUTION */
		}

		for (i <- 1 to 7) {
			goAndGet();
			right();
			forward();
			left();
		}
		/* END TEMPLATE */
	} 
}
