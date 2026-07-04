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

        String[] args = command.split(" ");
        args = Arrays.copyOfRange(args, 1, args.length-1);

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
}
