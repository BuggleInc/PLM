package lessons.welcome.loopdowhile;

import java.awt.Color

import plm.universe.GridWorld
import plm.universe.bugglequest.BuggleWorldCell
import java.util.Locale
import plm.core.model.I18nManager
import org.xnap.commons.i18n.I18n

class ScalaPoucet2Entity extends plm.universe.bugglequest.SimpleBuggle {
	override def forward(i: Int)  {
	  val locale: Locale = getWorld.getLocale
		throw new RuntimeException(I18nManager.getI18n(locale).tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}
	override def backward(i: Int) {
	  val locale: Locale = getWorld.getLocale
	  val i18n: I18n = I18nManager.getI18n(locale)
		throw new RuntimeException(i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}

	def crossing(): Boolean = {
		val gridWorld = world.asInstanceOf[GridWorld];
	  	val here  = gridWorld.getCell(getX(),getY()).asInstanceOf[BuggleWorldCell];
		val right = gridWorld.getCell(  (getX()+1)%gridWorld.getWidth() ,  getY()  ).asInstanceOf[BuggleWorldCell]
		val below = gridWorld.getCell( getX()  ,  (getY()+1)%gridWorld.getHeight()).asInstanceOf[BuggleWorldCell]
		
		var open = 0;
		if (!here.hasLeftWall())
			open += 1;
		if (!here.hasTopWall())
			open += 1;
		if (!right.hasLeftWall())
			open += 1;
		if (!below.hasTopWall())
			open += 1;
		
		return open>2 || (here.hasLeftWall() != right.hasLeftWall()) || (here.hasTopWall() != below.hasTopWall());
	}
	def exitReached(): Boolean = {
		return getGroundColor().equals(Color.orange);
	}
	/* BINDINGS TRANSLATION */
	def sortieTrouvee(): Boolean = { return exitReached() }
	def croisement(): Boolean = { return crossing() }

	override def run() { 
		/* BEGIN SOLUTION */
		while (!exitReached()) {
			var count = 0;
			
			do {
				forward();
				if (isOverBaggle())
					count+=1;
			} while (! crossing());
			
			if (count>2)
				left();
			else
				right();
		}
		forward();
		/* END SOLUTION */
	}
}
