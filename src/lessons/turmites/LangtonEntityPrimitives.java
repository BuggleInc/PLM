package lessons.turmites;

import plm.core.lang.primitives.Primitive;
import plm.universe.bugglequest.AbstractBugglePrimitives;

public interface LangtonEntityPrimitives extends AbstractBugglePrimitives {
    @Primitive(240)
    void stepDone();
}
