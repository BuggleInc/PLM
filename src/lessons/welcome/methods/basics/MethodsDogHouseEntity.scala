package lessons.welcome.methods.basics;

import plm.universe.bugglequest.SimpleBuggle

class MethodsDogHouseEntity extends SimpleBuggle {
	/* BEGIN REMOTE */
	/* BEGIN SOLUTION */
	def dogHouse(): Unit = {
		for (i <- 1 to 4) {
			step()
			step()
			left()
		}
	}
	/* END SOLUTION */

	override def run(): Unit = {
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
