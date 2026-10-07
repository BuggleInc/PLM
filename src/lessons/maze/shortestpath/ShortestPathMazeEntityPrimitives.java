package lessons.maze.shortestpath;

import plm.core.lang.primitives.Primitive;
import plm.universe.bugglequest.AbstractBugglePrimitives;

public interface ShortestPathMazeEntityPrimitives extends AbstractBugglePrimitives {
  @Primitive void setIndication(int x, int y, int i);

  @Primitive int getIndication(int x, int y);

  @Primitive boolean hasBaggle(int x, int y);
}
