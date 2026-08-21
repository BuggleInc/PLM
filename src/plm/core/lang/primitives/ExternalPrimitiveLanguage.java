package plm.core.lang.primitives;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ExternalPrimitiveLanguage {

  static Set<Class<?>> involved(List<PrimitiveMethod> methods)
  {
    Set<Class<?>> types = new HashSet<>();
    for (PrimitiveMethod method : methods) {
      types.add(method.output());
      for (PrimitiveParameter parameter : method.parameters()) {
        types.add(parameter.type());
      }
    }
    types.remove(null);

    // It may happen that a type is only involved as an array of itself and not directly. For example, LanderWorld have a
    // primitive returning "Point[]" but no primitive involves "Point" directly. So add the component types too.
    for (Class<?> type : List.copyOf(types))
      if (type.isArray())
        types.add(type.getComponentType());

    return types;
  }

  void generate(File folder, String name, List<PrimitiveMethod> methods) throws IOException;

  /**
   * Same as {@link #generate(File, String, List)}, but with a string added verbatim to the generated class body. This is to
   * add hand-written helpers on need.
   */
  default void generate(File folder, String name, List<PrimitiveMethod> methods, String extraCode) throws IOException { generate(folder, name, methods); }
}
