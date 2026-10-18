package lessons.turmites.universe;

import java.awt.Color;
import java.util.List;
import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Glue;
import plm.core.model.Game;
import plm.universe.Direction;
import plm.universe.bugglequest.BuggleWorld;
import plm.universe.bugglequest.SimpleBuggle;

@Glue("RemoteTurmite")
@EntityPrimitives(TurmiteEntityPrimitives.class)
public class TurmiteEntity extends SimpleBuggle implements TurmiteEntityPrimitives {
  public TurmiteEntity(BuggleWorld buggleWorld, String name, int buggleX, int buggleY, Direction dir, Color col1, Color col2)
  {
    super(buggleWorld, name, buggleX, buggleY, dir, col1, col2);
    forbidBuggleExtras();
  }
  public TurmiteEntity()
  {
    super();
    forbidBuggleExtras();
  }
  @Override public void stepDone() { ((lessons.turmites.universe.TurmiteWorld)world).stepDone(); }

  /**
   * Forbids the buggle primitives that turmites don't need, which also hides them from the documentation of the world. Turmites only
   * turn, step, read the ground and paint it, and their initial position can be set.
   */
  protected void forbidBuggleExtras()
  {
    String reason = Game.i18n.tr("Turmites don't need it.");
    for (String primitive :
         List.of("getX", "getY", "forward", "stepBack", "backward", "getDirection", "setDirection", "isSelected", "getBodyColor", "setBodyColor",
                 "isFacingWall", "isBackingWall", "isBrushDown", "getBrushColor", "getWorldWidth", "getWorldHeight", "isOverBaggle", "isCarryingBaggle",
                 "pickupBaggle", "dropBaggle", "isOverMessage", "writeMessage", "readMessage", "clearMessage", "hasTopWall", "hasLeftWall"))
      forbid(primitive, reason);
  }
}
