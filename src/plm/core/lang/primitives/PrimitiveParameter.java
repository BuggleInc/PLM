package plm.core.lang.primitives;

import java.lang.reflect.Parameter;

/** A parameter of a primitive, as declared in Java. */
public record PrimitiveParameter(String name, Class<?> type)
{
  public PrimitiveParameter(Parameter parameter)
  {
    this(parameter.getName(), parameter.getType());
  }

  @Override public String toString()
  {
    return name + ": " + type.getSimpleName();
  }
}
