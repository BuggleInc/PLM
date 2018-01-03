package unittest;

import plm.universe.unittest.AbstractExampleEntity;

public class ExampleEntity extends AbstractExampleEntity {

	@Override
	/* BEGIN TEMPLATE */
	public void run() {
		/* BEGIN SOLUTION */
		System.out.println("Demo is running");
		setObjective(true);
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
