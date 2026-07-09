package plm.universe;

import plm.core.lang.primitives.CommandArgumentType;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveParameter;
import plm.core.lang.primitives.PrimitiveRegistration;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class CommandExecutor {
    private CommandExecutor() {
    }

    private static final Map<Class<? extends Entity>, Map<Integer, PrimitiveMethod>> getMinimalPrimitiveForEntity_Cache = new HashMap<>();

    public synchronized static void command(Entity entity, String command, BufferedWriter out) throws InvocationTargetException, IllegalAccessException {
        if (command.contains("AddressSanitizer")) {
            if (!command.equals("AddressSanitizer:DEADLYSIGNAL"))
                System.err.println(command);
            return;
        }

        int id = Integer.parseInt(command.substring(0, command.indexOf(' ')));
        String primitive = command.substring(command.lastIndexOf(' ') + 1);
        PrimitiveMethod method = getMinimalPrimitiveForEntity_Cache.computeIfAbsent(entity.getClass(), PrimitiveRegistration::getMinimalPrimitiveForEntity)
                .get(id);

        if (!method.name().equals(primitive)) {
            System.err.println("Primitive real name do not match provided name. (expected: " + method.name() + ", provided: " + primitive + ")");
            return;
        }

        // The middle part (between the leading id and the trailing primitive name) contains the serialized arguments, space-separated.
        // But a serialized String argument may itself contain spaces (e.g. "Oh Boy!"), using a simple split(" ") would ruin the parameter.
        // Instead, tokenize the middle part while respecting quoted strings and bracketed arrays.
        // FIXME: we should use ValueSerializer for the whole array of parameters, but this requires to implement this logic in C too
        int firstSpace  = command.indexOf(' ');
        int lastSpace   = command.lastIndexOf(' ');
        String argsPart = (firstSpace < lastSpace) ? command.substring(firstSpace + 1, lastSpace) : "";
        String[] args   = splitArgsRespectingQuotesAndBrackets(argsPart);

        Method javaMethod = method.getMethod();
        List<PrimitiveParameter> parameters = method.parameters();
        List<Object> javaParameters = new ArrayList<>();

        for (int i = 0; i < parameters.size(); i++) {
            PrimitiveParameter parameter = parameters.get(i);

            Object value = parameter.type().deserialize(args[i]);
            javaParameters.add(value);
        }

        Object returnValue = javaMethod.invoke(entity, javaParameters.toArray());
        @SuppressWarnings("unchecked") CommandArgumentType<Object> returnType = (CommandArgumentType<Object>) method.output();

        try {
            if (returnType != null) {
                String serialize = returnType.serialize(returnValue);
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
    private static String[] splitArgsRespectingQuotesAndBrackets(String argsPart)
    {
      if (argsPart.isEmpty())
        return new String[0];

      List<String> tokens   = new ArrayList<>();
      StringBuilder current = new StringBuilder();
      boolean inQuotes      = false;
      int bracketDepth      = 0;

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
