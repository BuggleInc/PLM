package lessons.sort.dutchflag.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.EntityPrimitivesBase;

public interface DutchFlagEntityPrimitives extends EntityPrimitivesBase {
  @Primitive(value = "Swaps the given lines.") void swap(int fromLine, int toLine);

  @Primitive(value = "Returns the color of the given line.") int getColor(int rank);

  @Primitive(value = "Returns the amount of lines of the flag.") int getSize();

  @Primitive(value = "Tells whether the flag is sorted.") boolean isSorted();

  @Primitive(value = "Tells whether the current world is selected in the interface.") boolean isSelected();

  @Primitive void assertSorted();
}
