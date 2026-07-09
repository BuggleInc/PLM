package plm.core.lang.primitives;

import plm.core.utils.ColorMapper;
import plm.core.utils.InvalidColorNameException;
import plm.universe.Direction;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public abstract class CommandArgumentType<T> {
    public static final CommandArgumentType<Integer> INT = new CommandArgumentType<>(0, "INT", int.class) {
        @Override
        public String serialize(Integer value) {
            return String.valueOf(value);
        }

        @Override
        public Integer deserialize(String value) {
            return Integer.parseInt(value);
        }
    };
    public static final CommandArgumentType<Double> DOUBLE = new CommandArgumentType<>(1, "DOUBLE", double.class) {
        @Override
        public String serialize(Double value) {
            return String.valueOf(value);
        }

        @Override
        public Double deserialize(String value) {
            return Double.parseDouble(value);
        }
    };
    public static final CommandArgumentType<String> STRING = new CommandArgumentType<>(2, "STRING", String.class) {
        @Override
        public String serialize(String value) {
            return value;
        }

        @Override
        public String deserialize(String value) {
            return value;
        }
    };
    public static final CommandArgumentType<Character> CHAR = new CommandArgumentType<>(3, "CHAR", char.class) {
        @Override
        public String serialize(Character value) {
            return String.valueOf(value);
        }

        @Override
        public Character deserialize(String value) {
            return value.charAt(0);
        }
    };
    public static final CommandArgumentType<Boolean> BOOLEAN = new CommandArgumentType<>(4, "BOOLEAN", boolean.class) {
        @Override
        public String serialize(Boolean value) {
            return value ? "1" : "0";
        }

        @Override
        public Boolean deserialize(String value) {
            return value.equals("1");
        }
    };

    public static final CommandArgumentType<Color> COLOR = new CommandArgumentType<>(5, "COLOR", Color.class) {
        @Override
        public String serialize(Color value) {
            return String.valueOf(ColorMapper.color2int(value));
        }

        @Override
        public Color deserialize(String value) {
            if (value.indexOf('/') >= 0) {
                try {
                    return ColorMapper.name2color(value);
                } catch (InvalidColorNameException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    return ColorMapper.int2color(Integer.parseInt(value));
                } catch (InvalidColorNameException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    public static final CommandArgumentType<Direction> DIRECTION =
            new CommandArgumentType<>(6, "DIRECTION", Direction.class) {
                @Override
                public String serialize(Direction value) {
                    return String.valueOf(value.ordinal());
                }

                @Override
                public Direction deserialize(String value) {

                    int nb = Integer.parseInt(value);
                    return Direction.values()[nb];
                }
            };

    private final int ordinal;
    private final String name;
    private final Class<T> clazz;

    CommandArgumentType(int ordinal, String name, Class<T> clazz) {
        this.ordinal = ordinal;
        this.name = name;
        this.clazz = clazz;
    }

    public static CommandArgumentType<?>[] values() {
        return new CommandArgumentType[]{INT, DOUBLE, STRING, COLOR, DIRECTION, CHAR, BOOLEAN};
    }

    public static CommandArgumentType<?> getCommandArgumentTypeFromClass(Class<?> clazz) {
        return Arrays.stream(values()).filter(type -> type.clazz == clazz || clazz.isAssignableFrom(type.clazz)).findFirst().orElse(null);
    }

    public static <T> String findTypeAndSerialize(T value) {

        @SuppressWarnings("unchecked") CommandArgumentType<T> type = (CommandArgumentType<T>) getCommandArgumentTypeFromClass(value.getClass());
        if (type == null) return value.toString();
        return type.serialize(value);
    }

    public abstract String serialize(T value);

    public abstract T deserialize(String value);

    public int ordinal() {
        return ordinal;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name.charAt(0) + name.toLowerCase().substring(1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CommandArgumentType<?>) obj;
        return Objects.equals(this.ordinal, that.ordinal);
    }
}
