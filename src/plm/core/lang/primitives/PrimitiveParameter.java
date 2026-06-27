package plm.core.lang.primitives;

import java.lang.reflect.Parameter;
import java.util.Objects;

public final class PrimitiveParameter {
    private final String name;
    private final CommandArgumentType<?> type;

    public PrimitiveParameter(Parameter parameter) {
        this.name = parameter.getName();
        this.type = CommandArgumentType.getCommandArgumentTypeFromClass(parameter.getType());
    }

    public String name() {
        return this.name;
    }

    public CommandArgumentType<?> type() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PrimitiveParameter) obj;
        return Objects.equals(this.name, that.name) && Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return name + ": " + type;
    }

}
