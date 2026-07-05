package lessons.recursion.lego.spiral;

import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Primitive;
import plm.universe.turtles.Turtle;

@EntityPrimitives(SpiralUseEntity.class)
public class SpiralUseEntity extends Turtle {

	/* BEGIN TEMPLATE */
	public void spiral(int steps, int angle, int length, int increment)	{
		/* BEGIN SOLUTION */
        if (steps > 0) {
            forward(length);
            left(angle);
            spiral(steps-1, angle, length+increment, increment);
        }
        /* END SOLUTION */
	}
	/* END TEMPLATE */

	public void run() {
		spiral(100,91,1,2);
	}
}
