package lessons.welcome.summative;

import plm.universe.bugglequest.SimpleBuggle;
import plm.core.model.I18nManager

class ScalaMoriaEntity extends SimpleBuggle {
	override def forward(i: Int)  { 
		throw new RuntimeException(I18nManager.getI18n(getWorld.getLocale).tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int) {
		throw new RuntimeException(I18nManager.getI18n(getWorld.getLocale).tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

	override def run() {
		/* BEGIN SOLUTION */
		back();
		while (!isFacingWall()) {
			while (!isOverBaggle() && !isFacingWall())
				forward();
			if (isOverBaggle()) {
				pickupBaggle();
				back();
				while (!isOverBaggle())
					forward();
				backward();
				dropBaggle();
				back();
				forward();
			}
		}
		right();
		forward();
		left();
		forward();
		/* END SOLUTION */
	}
}
