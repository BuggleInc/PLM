package lessons.welcome.methods.basics;

import plm.universe.bugglequest.SimpleBuggle
import plm.core.model.Game
import scala.collection.JavaConversions
import plm.core.model.lesson.Exercise

class MethodsDogHouseEntity extends SimpleBuggle {
	/* BEGIN REMOTE */
	/* BEGIN SOLUTION */
	def dogHouse() {
		for (i <- 1 to 4) {
			stepForward()
			stepForward()
			left()
		}
	}
	/* END SOLUTION */

	override def run() {
		brushDown();
		dogHouse();
		brushUp();

		forward(4);

		brushDown();
		dogHouse();		
		brushUp();

		forward(2);
		left();
		forward(4);

		brushDown();
		dogHouse();		
		brushUp();

		forward(2);
		left();
		forward(4);

		brushDown();
		dogHouse();		
	} 
	/* END REMOTE */
}
