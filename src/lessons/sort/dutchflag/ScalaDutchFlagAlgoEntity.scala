package lessons.sort.dutchflag;

import plm.core.model.Game
import plm.universe.dutchflag.DutchFlagEntity
import plm.universe.dutchflag.DutchFlagEntity._
import plm.universe.dutchflag.DutchFlagWorld

class ScalaDutchFlagAlgoEntity extends DutchFlagEntity {
	
	override def run() {
		solve();
	}

	/* BEGIN TEMPLATE */
	def solve() {
		/* BEGIN SOLUTION */
		var afterBlue=0;
		var beforeWhite=getSize()-1;
		var beforeRed=getSize()-1;
		while (afterBlue <= beforeWhite) {
			
			getColor(afterBlue) match {
			case BLUE =>
				afterBlue += 1
			case WHITE =>
				swap(afterBlue, beforeWhite)
				beforeWhite -= 1
			case RED =>
				swap(afterBlue, beforeWhite)
				swap(beforeRed, beforeWhite)
				beforeWhite -= 1
				beforeRed -= 1
			}
		}
		world.asInstanceOf[DutchFlagWorld].assertSorted();
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
