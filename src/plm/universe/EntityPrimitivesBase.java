package plm.universe;

import plm.core.ValueSerializer;
import plm.core.lang.primitives.Primitive;

public interface EntityPrimitivesBase {
    @Primitive(405)
    String getName();

    Object getParam(int i);

    int getParamCount();

    boolean isSelected();


    @Primitive(401)
    default double getParamDouble(int i) {
        return (Double) getParam(i);
    }

    @Primitive(402)
    default int getParamInt(int i) {
        return (Integer) getParam(i);
    }

    @Primitive(403)
    default boolean getParamBoolean(int i) {
        return (Boolean) getParam(i);
    }

    @Primitive(404)
    default String getParamString(int i) {
        return (String) getParam(i);
    }

    @Primitive(406)
    default String getParamSerialized(int i) {
        return ValueSerializer.serialize(getParam(i));
    }
}
