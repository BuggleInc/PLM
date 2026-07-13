package plm.universe;

import plm.core.ValueSerializer;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveRegistration;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommandExecutor {
    private static final Map<Class<? extends Entity>, Map<Integer, PrimitiveMethod>> getMinimalPrimitiveForEntity_Cache = new HashMap<>();

    private CommandExecutor() {
    }

    public synchronized static void command(Entity entity, String command, BufferedWriter out) throws InvocationTargetException, IllegalAccessException {
        if (command.contains("AddressSanitizer")) {
            if (!command.equals("AddressSanitizer:DEADLYSIGNAL"))
                System.err.println(command);
            return;
        }
        int firstSpace = command.indexOf(' ');
        int lastSpace = command.lastIndexOf(' ');

        String opCodeSegment = command.substring(0, firstSpace);
        String opArgsSegment = firstSpace <= lastSpace + 1 ? command.substring(firstSpace + 1, lastSpace) : "";
        String opNameSegment = command.substring(lastSpace + 1);

        int opCode = Integer.parseInt(opCodeSegment);
        Map<Integer, PrimitiveMethod> primitiveMethodMap = getMinimalPrimitiveForEntity_Cache
                .computeIfAbsent(entity.getClass(), PrimitiveRegistration::getMinimalPrimitiveForEntity);
        PrimitiveMethod method = primitiveMethodMap.get(opCode);

        if (!method.name().equals(opNameSegment)) {
            System.err.println("Primitive real name do not match provided name. " +
                    "(expected: " + method.name() + ", provided: " + opNameSegment + ")");
            return;
        }

        // The middle part (between the leading id and the trailing primitive name) contains the serialized arguments, space-separated.
        // But a serialized String argument may itself contain spaces (e.g. "Oh Boy!"), using a simple split(" ") would ruin the parameter.
        // Instead, tokenize the middle part while respecting quoted strings and bracketed arrays.
        // FIXME: we should use ValueSerializer for the whole array of parameters, but this requires to implement this logic in C too
        Object[] args = (Object[]) ValueSerializer.deserialize(opArgsSegment);

        for (int i = 0; i < args.length; i++) {
            Object rawArg = args[i];
            Class<?> argType = method.parameters().get(i).type();

            if (argType.isEnum()) {
                try {
                    args[i] = ((Object[]) argType.getMethod("values").invoke(null))[(int) rawArg];
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Method javaMethod = method.method();
        Object returnValue = javaMethod.invoke(entity, args);

        try {
            if (method.hasReturn()) {

                if (returnValue instanceof Enum<?>) {
                    returnValue = ((Enum<?>) returnValue).ordinal();
                }

                String serialize = ValueSerializer.serialize(returnValue);
                out.write(serialize);
                out.write("\n");
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Splits a space-separated list of serialized argument tokens, without breaking tokens that contain spaces inside a quoted
     * string (e.g. "a b") or inside a bracketed array (e.g. [2:"a b":i3]). Respects backslash-escaping of quotes as produced by
     * ValueSerializer.serialize (\\ and \").
     */
    private static String[] splitArgsRespectingQuotesAndBrackets(String argsPart) {
        if (argsPart.isEmpty())
            return new String[0];

        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        int bracketDepth = 0;

        for (int i = 0; i < argsPart.length(); i++) {
            char c = argsPart.charAt(i);

            if (inQuotes) {
                current.append(c);
                if (c == '\\' && i + 1 < argsPart.length()) {
                    // Keep the escaped character glued to its backslash to differentiate an escaped quote from for the string end
                    current.append(argsPart.charAt(++i));
                } else if (c == '"') {
                    inQuotes = false;
                }
                continue;
            }

            if (c == '"') {
                inQuotes = true;
                current.append(c);
            } else if (c == '[') {
                bracketDepth++;
                current.append(c);
            } else if (c == ']') {
                bracketDepth--;
                current.append(c);
            } else if (c == ' ' && bracketDepth == 0) {
                tokens.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        tokens.add(current.toString());

        return tokens.toArray(new String[0]);
    }
}
