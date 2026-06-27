package plm.core.lang.primitives;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ExternalPrimitiveLanguage {

    static Set<CommandArgumentType<?>> involved(List<PrimitiveMethod> methods) {
        Set<CommandArgumentType<?>> types = new HashSet<>();
        for (PrimitiveMethod method : methods) {
            types.add(method.output());
            for (PrimitiveParameter parameter : method.parameters()) {
                types.add(parameter.type());
            }
        }

        types.remove(null);
        return types;
    }

    void generate(File folder, String name, List<PrimitiveMethod> methods) throws IOException;
}
