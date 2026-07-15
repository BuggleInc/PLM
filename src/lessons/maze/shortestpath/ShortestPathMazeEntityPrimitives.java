package lessons.maze.shortestpath;

import plm.core.lang.primitives.Primitive;
import plm.universe.bugglequest.AbstractBugglePrimitives;

public interface ShortestPathMazeEntityPrimitives extends AbstractBugglePrimitives {
  @Primitive(143) void setIndication(int x, int y, int i);

  @Primitive(144) int getIndication(int x, int y);

  @Primitive(145) boolean hasBaggle(int x, int y);
}
