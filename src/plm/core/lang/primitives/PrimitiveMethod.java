package plm.core.lang.primitives;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class PrimitiveMethod {
  private final List<PrimitiveParameter> parameters;
  private final String name;
  private final String location;
  private final Method method;
  private final Class<?> output;

  public PrimitiveMethod(Primitive primitive, Method method)
  {
    this.name       = primitive.name().isEmpty() ? method.getName() : primitive.name();
    this.location   = method.getDeclaringClass().getSimpleName() + "::" + name();
    this.method     = method;
    this.parameters = Arrays.stream(method.getParameters()).map(PrimitiveParameter::new).toList();
    this.output     = method.getReturnType();
  }

  public Class<?> output() { return output; }

  public String name() { return name; }

  public String location() { return location; }

  public List<PrimitiveParameter> parameters() { return parameters; }

  public boolean hasReturn() { return output != void.class; }

  @Override public boolean equals(Object obj)
  {
    if (obj == this)
      return true;
    if (obj == null || obj.getClass() != this.getClass())
      return false;
    var that = (PrimitiveMethod)obj;
    return Objects.equals(name, that.name) && Objects.equals(parameters, that.parameters);
  }

  @Override public int hashCode() { return Objects.hash(name, parameters); }

  @Override public String toString()
  {
    return name() + "(" + parameters.stream().map(PrimitiveParameter::toString).collect(Collectors.joining(",")) + ")"
        + ":" + output.getSimpleName();
  }

  public Method method() { return method; }
}
