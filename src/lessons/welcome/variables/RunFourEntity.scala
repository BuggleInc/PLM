package lessons.welcome.variables;


class RunFourEntity extends plm.universe.bugglequest.SimpleBuggle {

	/* BEGIN REMOTE */
	override def run(): Unit = {
		/* BEGIN SOLUTION */
		var cpt = 0;
		while (cpt != 4) {
			stepForward();
			if (isOverBaggle())
				cpt += 1
		}
		/* END SOLUTION */
	}
	/* END REMOTE */
}
