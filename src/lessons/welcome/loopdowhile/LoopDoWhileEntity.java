package lessons.welcome.loopdowhile;

import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Primitive;

import java.awt.Color;

@EntityPrimitives(LoopDoWhileEntity.class)
public class LoopDoWhileEntity extends plm.universe.bugglequest.SimpleBuggle {

	@Primitive(301)
	boolean isGroundWhite() { 
		return getGroundColor()== Color.white?true:false;
	}
	/* BINDINGS TRANSLATION */
	boolean estSurBlanc() { return isGroundWhite() ; }
	
	@Override
	/* BEGIN TEMPLATE */
	public void run() { 
		/* BEGIN SOLUTION */
		do {
			stepForward();
		} while (!isGroundWhite());
		/* END SOLUTION */
	}
	/* END TEMPLATE */
}
