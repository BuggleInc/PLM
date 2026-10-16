package lessons.welcome.variables;

class VariablesEntity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		var stepper = 0;
		while (!isOverBaggle()) {
			stepper += 1
			step()
		}
		pickupBaggle();
		while (stepper>0) {
			stepBack()
			stepper -= 1
		}
		dropBaggle();
		/* END SOLUTION */
	}
	/* END REMOTE */
}
