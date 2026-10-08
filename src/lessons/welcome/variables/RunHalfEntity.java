package lessons.welcome.variables;

import java.awt.Color;
import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Primitive;

@EntityPrimitives(RunHalfEntity.class)
public class RunHalfEntity extends plm.universe.bugglequest.SimpleBuggle {
  public RunHalfEntity() { forbidMultiStep(); }

  @Primitive public boolean isOverOrange() { return getGroundColor() == Color.orange; }
  /* BINDINGS TRANSLATION */
  public boolean estSurOrange() { return isOverOrange(); }

  @Override
  /* BEGIN REMOTE */
  /* BEGIN TEMPLATE */
  public void run()
  {
    /* BEGIN SOLUTION */
    int baggle = 0;
    int orange = 0;
    while (2 * baggle != orange + 1) {
      // if (getName().equals("buggle2"))
      //	System.out.println("baggle: "+baggle+"; orange: "+orange+"; sum:"+(2*baggle-orange-1));
      stepForward();
      if (isOverBaggle())
        baggle++;
      if (isOverOrange())
        orange++;
    }
    /* END SOLUTION */
  }
  /* END TEMPLATE */
  /* END REMOTE */
}
