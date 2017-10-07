package lessons.welcome.loopwhile;

import plm.universe.bugglequest.SimpleBuggle

import java.util.Locale
import plm.core.model.I18nManager
import org.xnap.commons.i18n.I18n

class ScalaLoopWhileEntity extends SimpleBuggle {
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
		while (!isFacingWall())
			forward();
		/* END SOLUTION */
	}
}
