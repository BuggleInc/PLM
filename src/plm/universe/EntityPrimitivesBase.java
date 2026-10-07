package plm.universe;

import plm.core.ValueSerializer;
import plm.core.lang.primitives.Primitive;

public interface EntityPrimitivesBase {
  @Primitive String getName();

  Object getParam(int i);

  int getParamCount();

  boolean isSelected();

  @Primitive default double getParamDouble(int i) { return (Double)getParam(i); }

  @Primitive default int getParamInt(int i) { return (Integer)getParam(i); }

  @Primitive default boolean getParamBoolean(int i) { return (Boolean)getParam(i); }

  @Primitive default String getParamString(int i) { return (String)getParam(i); }

  @Primitive default String getParamSerialized(int i) { return ValueSerializer.serialize(getParam(i)); }
}
