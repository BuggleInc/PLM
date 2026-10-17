package lessons.sort.baseball.universe;

import plm.core.lang.primitives.Primitive;
import plm.core.lang.primitives.PrimitiveGroups;
import plm.universe.EntityPrimitivesBase;

@PrimitiveGroups({"Dimensions of the world", "State of the world", "Changing the world"})
public interface BaseballEntityPrimitives extends EntityPrimitivesBase {
  @Primitive(value = "Returns the amount of bases on the field.", group = "Dimensions of the world") int getBasesAmount();

  @Primitive(value = "Returns the amount of player positions per base.", group = "Dimensions of the world") int getPositionsAmount();

  @Primitive(value = "Returns the color of the player at the given location.", group = "State of the world") int getPlayerColor(int base, int position);

  @Primitive(value = "Tells whether all players of the given base are at home.", group = "State of the world") boolean isBaseSorted(int base);

  @Primitive(value = "Tells whether all players of the field are at home.", group = "State of the world") boolean isSorted();

  @Primitive(value = "Returns the base in which the hole is.", group = "State of the world") int getHoleBase();

  @Primitive(value = "Returns the position of the hole within its base.", group = "State of the world") int getHolePosition();

  @Primitive(value = "Moves the given player into the hole. Fails if the player is not next to the hole (at most one base away).", group = "Changing the world")
  void move(int base, int position);

  @Primitive(value = "Tells whether the current world is selected in the interface.", group = "State of the world") @Override boolean isSelected();

  @Primitive void assertSorted(String str);
}
