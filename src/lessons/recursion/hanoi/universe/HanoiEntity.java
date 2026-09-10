package lessons.recursion.hanoi.universe;
/* BEGIN TEMPLATE */
import plm.core.lang.primitives.EntityPrimitives;
import plm.core.lang.primitives.Primitive;
import plm.core.model.Game;
import plm.universe.Entity;
import plm.universe.World;

@EntityPrimitives(HanoiEntityPrimitives.class)
public class HanoiEntity extends Entity implements HanoiEntityPrimitives {
  /**
   * Instantiation Constructor (used by exercises to setup the world)
   * Must call super(name, world). If you had fields to setup, you'd  have to add more parameters
   */
  public HanoiEntity(String name, World world)
  {
    /* BEGIN HIDDEN */
    super(name, world);
    /* END HIDDEN */
  }

  /**
   * Part of the copy process
   * Must call super(name)
   */
  public HanoiEntity(String name)
  {
    /* BEGIN HIDDEN */
    super(name);
    /* END HIDDEN */
  }
  /** Must exist too. Calling HanoiEntity("dummy name") is ok */
  public HanoiEntity()
  {
    /* BEGIN HIDDEN */
    this("Hanoi Entity");
    /* END HIDDEN */
  }

  /** Must exist so that exercises can instantiate your entity (Entity is abstract) */
  @Override public void run() {}

  /** Part of your world logic */
  @Override public void move(int src, int dst) { regularMove(src, dst); }
  public void regularMove(int src, int dst)
  {
    /* BEGIN HIDDEN */
    ((HanoiWorld)world).move(src, dst);
    stepUI();
    /* END HIDDEN */
  }
  public void cyclicMove(int src, int dst)
  {
    if ((src == 0 && dst != 1) || (src == 1 && dst != 2) || (src == 2 && dst != 0))
      throw new RuntimeException(Game.i18n.tr(
          "Sorry Dave, I cannot let you move disks counterclockwise. Move from 0 to 1, from 1 to 2 or from 2 to 0 only, not from {0} to {1}.", src, dst));
    regularMove(src, dst);
  }
  /** Returns the amount of disks on the given slot */
  @Override public int getSlotSize(int slot) { return ((HanoiWorld)world).getSlotSize(slot); }
  /** Returns the radius of the topmost disk of the given slot */

  @Primitive(307) public int getSlotRadius(int slot) { return ((HanoiWorld)world).getRadius(slot); }

  /* BEGIN HIDDEN */
  @Override public String toString() { return "HanoiEntity (" + this.getClass().getName() + ")"; }
  /* END HIDDEN */

  /* BINDINGS TRANSLATION: French */
  public void deplace(int src, int dst) { move(src, dst); }
  public int getTaillePiquet(int rank) { return getSlotSize(rank); }
}
/* END TEMPLATE */
