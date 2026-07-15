package plm.test.simple;

import plm.core.lang.primitives.EntityPrimitives;
import plm.universe.Entity;

@EntityPrimitives(SimpleEntityPrimitives.class)
public class SimpleExerciseEntity extends Entity implements SimpleEntityPrimitives {

  @Override public void setObjectif(boolean b) { ((SimpleWorld)world).setObjectif(b); }

  @Override
  /* BEGIN TEMPLATE */
  public void run() throws Exception
  {
    /* BEGIN SOLUTION */
    setObjectif(true);
    /* END SOLUTION */
  }
  /* END TEMPLATE */
}
