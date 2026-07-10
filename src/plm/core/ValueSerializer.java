package plm.core;

import java.util.*;
import java.util.stream.Collectors;

public class ValueSerializer {

    private static String getTypeRepresentation(Class<?> clazz) {
        assert !clazz.isArray();

        if (clazz == Integer.class || clazz == int.class) return "i";
        if (clazz == Double.class || clazz == double.class) return "f";
        if (clazz == Boolean.class || clazz == boolean.class) return "b";

        if (clazz == Object.class || clazz == String.class) return "";
        throw new IllegalArgumentException("Unknown serializable type: " + clazz.getSimpleName());
    }

    /* --- Serialization logic --- */
    public static String serialize(Object o) {
        if (o == null) return "Z";

        Class<?> clazz = o.getClass();
        if (o instanceof Collection || clazz.isArray()) {

            List<?> values;
            String typeRepresentation;

            if (o instanceof Collection<?> objects) {
                values = Arrays.asList(objects.toArray());
                typeRepresentation = objects.isEmpty() ? "" : getTypeRepresentation(objects.iterator().next().getClass());
            } else {
                typeRepresentation = getTypeRepresentation(clazz.getComponentType());
                if (o instanceof int[] ints) {
                    values = Arrays.stream(ints).boxed().toList();
                } else if (o instanceof double[] doubles) {
                    values = Arrays.stream(doubles).boxed().toList();
                } else if (o instanceof boolean[] booleans) {
                    // Arrays.stream is not implemented for booleans...
                    List<Boolean> booleanValues = new ArrayList<>();
                    for (boolean b : booleans) {
                        Boolean boo = b ? Boolean.TRUE : Boolean.FALSE;
                        booleanValues.add(boo);
                    }
                    values = booleanValues;
                } else {
                    values = Arrays.stream((Object[]) o).toList();
                }
            }

            return typeRepresentation +
                    "[" +
                    values.size() +
                    values.stream().map(ValueSerializer::serialize).map(s -> ":" + s).collect(Collectors.joining()) +
                    "]";
        }

        if (o instanceof String value) {
            String escaped = value
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"");
            return "\"" + escaped + "\"";
        }

        String typeRepresentation = getTypeRepresentation(clazz);

        if (typeRepresentation.equals("b")) {
            return "b" + (Objects.equals(o, true) ? "1" : "0");
        }
        return typeRepresentation + o;
    }

    /* --- Deserialization logic --- */

    public static Object deserialize(String text) {
        Parser parser = new Parser(text);
        Object value = parser.parseValue();
        if (!parser.isFinished()) {
            throw new IllegalArgumentException("Unexpected trailing characters.");
        }
        return value;
    }

    private static final class Parser {

        private final String text;
        private int pos = 0;

        Parser(String text) {
            this.text = text;
        }

        private static boolean isNumberChar(char c) {
            return Character.isDigit(c) || c == '-';
        }

        private static boolean isDoubleChar(char c) {
            return Character.isDigit(c)
                    || c == '-'
                    || c == '.'
                    || c == 'e'
                    || c == 'E'
                    || c == '+';
        }

        /* ---------- Arrays ---------- */

        boolean isFinished() {
            return pos == text.length();
        }

        Object parseValue() {
            char c = peek();

            if (c == 'Z') {
                pos++;
                return null;
            }

            if (c == '"') {
                return parseString();
            }

            if (c == 'i') {
                pos++;
                if (peek() == '[') {
                    return parseIntArray();
                }
                return parseInt();
            }

            if (c == 'f') {
                pos++;
                if (peek() == '[') {
                    return parseDoubleArray();
                }
                return parseDouble();
            }

            if (c == 'b') {
                pos++;
                if (peek() == '[') {
                    return parseBooleanArray();
                }
                return parseBoolean();
            }

            if (c == '[') {
                return parseObjectArray();
            }

            throw new IllegalArgumentException("Unexpected character '" + c + "' at " + pos);
        }

        private int[] parseIntArray() {
            Object[] values = parseArrayContents();
            int[] result = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                result[i] = (Integer) values[i];
            }
            return result;
        }

        private double[] parseDoubleArray() {
            Object[] values = parseArrayContents();
            double[] result = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                result[i] = (Double) values[i];
            }
            return result;
        }

        private boolean[] parseBooleanArray() {
            Object[] values = parseArrayContents();
            boolean[] result = new boolean[values.length];
            for (int i = 0; i < values.length; i++) {
                result[i] = (Boolean) values[i];
            }
            return result;
        }

        /* ---------- Primitive values ---------- */

        private Object[] parseObjectArray() {
            return parseArrayContents();
        }

        private Object[] parseArrayContents() {
            expect('[');

            int size = parseUnsignedInt();

            Object[] values = new Object[size];

            for (int i = 0; i < size; i++) {
                expect(':');
                values[i] = parseValue();
            }

            expect(']');

            return values;
        }

        private Integer parseInt() {
            int start = pos;
            while (!isFinished() && isNumberChar(peek())) {
                pos++;
            }
            return Integer.parseInt(text.substring(start, pos));
        }

        private Double parseDouble() {
            int start = pos;
            while (!isFinished() && isDoubleChar(peek())) {
                pos++;
            }
            return Double.parseDouble(text.substring(start, pos));
        }

        /* ---------- Helpers ---------- */

        private Boolean parseBoolean() {
            char c = peek();
            pos++;
            return switch (c) {
                case '0' -> false;
                case '1' -> true;
                default -> throw new IllegalArgumentException("Invalid boolean");
            };
        }

        private String parseString() {
            expect('"');

            StringBuilder sb = new StringBuilder();

            while (true) {
                if (isFinished()) {
                    throw new IllegalArgumentException("Unterminated string");
                }

                char c = text.charAt(pos++);

                if (c == '\\') {
                    if (isFinished()) {
                        throw new IllegalArgumentException("Invalid escape");
                    }
                    sb.append(text.charAt(pos++));
                } else if (c == '"') {
                    break;
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        }

        private int parseUnsignedInt() {
            int start = pos;
            while (!isFinished() && Character.isDigit(peek())) {
                pos++;
            }
            return Integer.parseInt(text.substring(start, pos));
        }

        private char peek() {
            return text.charAt(pos);
        }

        private void expect(char c) {
            if (isFinished() || text.charAt(pos) != c) {
                throw new IllegalArgumentException(
                        "Expected '" + c + "' at " + pos);
            }
            pos++;
        }
    }
}