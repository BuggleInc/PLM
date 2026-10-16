package lessons.maze.shortestpath;

import plm.core.lang.primitives.Primitive;
import plm.universe.bugglequest.AbstractBugglePrimitives;

public interface ShortestPathMazeEntityPrimitives extends AbstractBugglePrimitives {
  @Primitive(value = "Writes the given number on the given cell.", group = "Specific to this exercise") void setIndication(int x, int y, int i);

  @Primitive(value = "Returns the number written on the given cell.", group = "Specific to this exercise") int getIndication(int x, int y);

  @Primitive(value = "Tells whether there is a baggle on the given cell.", group = "Specific to this exercise") boolean hasBaggle(int x, int y);
}
