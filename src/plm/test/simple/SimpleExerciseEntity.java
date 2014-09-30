package plm.test.simple;

import java.io.BufferedWriter;

import plm.universe.Entity;
import plm.test.simple.SimpleWorld;

public class SimpleExerciseEntity extends Entity {

	@Override
	/* BEGIN TEMPLATE */
	public void run() throws Exception {
		/* BEGIN SOLUTION */
		((SimpleWorld) world).setObjectif(true);
		/* END SOLUTION */
	}
	/* END TEMPLATE */

	@Override
	public void command(String command, BufferedWriter out) {
	}
}
