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

    // It may happen that a type is only involved as an array of itself and not directly. For example, LanderWorld have a
    // primitive returning "Point[]" but no primitive involves "Point" directly. So add the component types too.
    for (Class<?> type : List.copyOf(types))
      if (type.isArray())
        types.add(type.getComponentType());

    return types;
  }

  /** Signature of the method in this language when called under the given name, as shown to the students (types, no keyword). */
  String signature(PrimitiveMethod method, String name);

  /** Writes the glue file(s) of the given name in folder, exposing the methods, with extraCode (hand-written helpers) added verbatim. */
  void generate(File folder, String name, List<PrimitiveMethod> methods, String extraCode) throws IOException;
}
