package lessons.welcome.methods.args;

import plm.universe.Direction
import plm.universe.bugglequest.SimpleBuggle

import java.util.Locale
import plm.core.model.I18nManager
import org.xnap.commons.i18n.I18n

class ScalaMethodsArgsEntity extends SimpleBuggle {
	override def forward(i: Int) {
	  val locale: Locale = getWorld.getLocale
	  val i18n: I18n = I18nManager.getI18n(locale)
		throw new RuntimeException(i18n.tr("I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int) {
	  val locale: Locale = getWorld.getLocale
	  val i18n: I18n = I18nManager.getI18n(locale)
		throw new RuntimeException(i18n.tr("I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}


	override def run() { 
		move(getY(),getDirection() == Direction.NORTH); 
	} 

	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	def move(steps: Int, goForward:Boolean) {
		if (goForward) {
			for (i <- 1 to steps) 
				forward()
		} else {
			for (i <- 1 to steps) 
				backward()
		}
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
