package lessons.turmites.universe;

import java.awt.Color;
import plm.core.lang.primitives.EntityPrimitives;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

@EntityPrimitives(TurmiteEntityPrimitives.class)
public class TurmiteEntity extends SimpleBuggle implements TurmiteEntityPrimitives {
  public TurmiteEntity(BuggleWorld buggleWorld, String name, int buggleX, int buggleY, Direction dir, Color col1, Color col2)
  {
    super(buggleWorld, name, buggleX, buggleY, dir, col1, col2);
  }
  public TurmiteEntity() { super(); }
  @Override public void stepDone() { ((lessons.turmites.universe.TurmiteWorld)world).stepDone(); }
}