package lessons.welcome.variables;

import java.awt.Color
import java.util.Locale
import plm.core.model.I18nManager
import org.xnap.commons.i18n.I18n

class ScalaRunHalfEntity extends plm.universe.bugglequest.SimpleBuggle {
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
	def isOverOrange():Boolean = {
		return getGroundColor().equals(Color.orange);
	}
	/* BINDINGS TRANSLATION */
	def estSurOrange():Boolean = { return isOverOrange(); }

	override def run() {
		/* BEGIN TEMPLATE */
		/* BEGIN SOLUTION */
		var baggle:Int = 0;
		var orange:Int = 0;
		while (2 * baggle != orange + 1) {
			//if (getName().equals("buggle2")) 
			//	getGame().getLogger().log("baggle: "+baggle+"; orange: "+orange+"; sum:"+(2*baggle-orange-1));
			forward();
			if (isOverBaggle())
				baggle += 1
			if (isOverOrange())
				orange += 1
		}
		/* END SOLUTION */
		/* END TEMPLATE */
	}
}
