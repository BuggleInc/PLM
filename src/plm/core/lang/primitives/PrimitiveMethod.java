package plm.core.lang.primitives;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class PrimitiveMethod {
    private final List<PrimitiveParameter> parameters;
    private final CommandArgumentType<?> output;
    private final String name;
    private final String location;
    public int id;

    public PrimitiveMethod(Primitive primitive, Method method) {
        this.id = primitive.value();
        this.name = primitive.name().isEmpty() ? method.getName() : primitive.name();
        this.location = method.getDeclaringClass().getSimpleName() + "::" + name();
        parameters = Arrays.stream(method.getParameters()).map(PrimitiveParameter::new).toList();
        output = Optional.of(method.getReturnType())
                .map(CommandArgumentType::getCommandArgumentTypeFromClass).orElse(null);
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String location() {
        return location;
    }

    public List<PrimitiveParameter> parameters() {
        return parameters;
    }

    public CommandArgumentType<?> output() {
        return output;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PrimitiveMethod) obj;
        return Objects.equals(name, that.name) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

    @Override
    public String toString() {
        return name() + "(" + parameters.stream().map(PrimitiveParameter::toString).collect(Collectors.joining(",")) + ")" + ":" + Optional.ofNullable(output).map(CommandArgumentType::toString).orElse("void");
    }
}
