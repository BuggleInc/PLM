package lessons.welcome.loopdowhile;

import java.awt.Color;
import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Primitive;

@EntityPrimitives(LoopDoWhileEntity.class)
public class LoopDoWhileEntity extends plm.universe.bugglequest.SimpleBuggle {

  @Primitive public boolean isGroundWhite() { return getGroundColor() == Color.white; }

  @Override
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    do {
      step();
    } while (!isGroundWhite());
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
