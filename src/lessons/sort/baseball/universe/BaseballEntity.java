package lessons.sort.baseball.universe;

import plm.core.lang.primitives.EntityPrimitives;
import plm.universe.Entity;

@EntityPrimitives(BaseballEntityPrimitives.class)
public class BaseballEntity extends Entity implements BaseballEntityPrimitives {
  /** Returns the amount of bases on your field */
  @Override public int getBasesAmount() { return ((BaseballWorld)this.world).getBasesAmount(); }
  /** Returns the amount of players locations available on each base of the field */
  @Override public int getPositionsAmount() { return ((BaseballWorld)this.world).getPositionsAmount(); }

  /** Returns the color of the player at the specified coordinate */
  @Override public int getPlayerColor(int base, int position) { return ((BaseballWorld)this.world).getPlayerColor(base, position); }
  /** Returns whether every players of the specified base are at home */
  @Override public boolean isBaseSorted(int base) { return ((BaseballWorld)this.world).isBaseSorted(base); }
  /** Returns if every player of the field is on the right base */
  @Override public boolean isSorted() { return ((BaseballWorld)this.world).isSorted(); }

  /** Returns the base in which the hole is located */
  @Override public int getHoleBase() { return ((BaseballWorld)this.world).getHoleBase(); }
  /** Returns the hole position within its base */
  @Override public int getHolePosition() { return ((BaseballWorld)this.world).getHolePosition(); }

  /**
   * Moves the specified player to the hole
   * @throws IllegalArgumentException if the specified player is not near the hole (at most one base away)
   */
  @Override public void move(int base, int position)
  {
    ((BaseballWorld)this.world).move(base, position);
    stepUI();
  }

  @Override public void assertSorted(String str) { ((BaseballWorld)world).assertSorted(str); }

  /** Must exist so that exercises can instantiate the entity (Entity is abstract) */
  @Override public void run() {}

  /** Returns a string representation of the world */
  public String toString() { return "BaseballEntity (" + this.getClass().getName() + ")"; }

  /* BINDINGS TRANSLATION: French */
  public int getNombreBases() { return getBasesAmount(); }
  public int getNombrePositions() { return getPositionsAmount(); }
  public int getCouleurJoueur(int base, int position) { return getPlayerColor(base, position); }
  public boolean estBaseTriee(int base) { return isBaseSorted(base); }
  public boolean estTrie() { return isSorted(); }

  public int getTrouBase() { return getHoleBase(); }
  public int getTrouPosition() { return getHolePosition(); }
  public void deplace(int base, int position) { move(base, position); }

  public boolean estSelectionne() { return isSelected(); }
}
