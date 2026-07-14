package lessons.welcome.loopdowhile;

import java.awt.Color;
import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Primitive;

@EntityPrimitives(LoopDoWhileEntity.class)
public class LoopDoWhileEntity extends plm.universe.bugglequest.SimpleBuggle {

  @Primitive(301) public boolean isGroundWhite() { return getGroundColor() == Color.white; }
  /* BINDINGS TRANSLATION */
  public boolean estSurBlanc() { return isGroundWhite(); }

  @Override
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    do {
      stepForward();
    } while (!isGroundWhite());
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
