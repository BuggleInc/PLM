package plm.core.lang.primitives;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class PrimitiveMethod {
  private final List<PrimitiveParameter> parameters;
  private final String name;
  private final String command;
  private final String location;
  private final Method method;
  private final Class<?> output;
  private final String doc;
  private final String group;

  public PrimitiveMethod(Method method)
  {
    this.name       = method.getName();
    this.command    = name;
    this.location   = method.getDeclaringClass().getSimpleName() + "::" + name();
    this.method     = method;
    this.parameters = Arrays.stream(method.getParameters()).map(PrimitiveParameter::new).toList();
    this.output     = method.getReturnType();
    this.doc        = method.getAnnotation(Primitive.class).value();
    this.group      = method.getAnnotation(Primitive.class).group();
  }

  /** English documentation, empty if the primitive is not documented. */
  public String doc() { return doc; }

  /** English title of the group under which the primitive is documented. */
  public String group() { return group; }

  private PrimitiveMethod(PrimitiveMethod primitive, String alias)
  {
    this.name       = alias;
    this.command    = primitive.command;
    this.location   = primitive.location;
    this.method     = primitive.method;
    this.parameters = primitive.parameters;
    this.output     = primitive.output;
    this.doc        = primitive.doc;
    this.group      = primitive.group;
  }

  /** The same primitive, called under a translated name in the students' code. */
  public PrimitiveMethod alias(String alias) { return new PrimitiveMethod(this, alias); }

  /** Name under which the glue calls the primitive in the PLM: the name of the Java method, even for the aliases. */
  public String command() { return command; }

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
