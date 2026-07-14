package lessons.turmites.universe;

import plm.core.lang.primitives.Primitive;
import plm.universe.bugglequest.AbstractBugglePrimitives;

public interface TurmiteEntityPrimitives extends AbstractBugglePrimitives {
  @Primitive(240) void stepDone();
}
