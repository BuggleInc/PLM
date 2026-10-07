package plm.universe;

import static plm.core.ValueSerializer.serialize;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import plm.core.ValueSerializer;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveRegistration;

public final class CommandExecutor {
  private static final Map<Class<? extends Entity>, Map<String, PrimitiveMethod>> getMinimalPrimitiveForEntity_Cache = new HashMap<>();

  private CommandExecutor() {}

  public synchronized static void command(Entity entity, String command, BufferedWriter out) throws InvocationTargetException, IllegalAccessException
  {
    if (command.contains("AddressSanitizer")) {
      if (!command.equals("AddressSanitizer:DEADLYSIGNAL"))
        System.err.println(command);
      return;
    }

    // The command is the primitive name, then the serialized array of the arguments. This array may contain spaces (e.g. in the
    // String "Oh Boy!"), which is why only the first space is a separator. The primitive name is a valid Java identifier so it
    // cannot contain any space.
    int firstSpace       = command.indexOf(' ');
    String opName        = command.substring(0, firstSpace);
    String opArgsSegment = command.substring(firstSpace + 1);

    Map<String, PrimitiveMethod> primitiveMethodMap =
        getMinimalPrimitiveForEntity_Cache.computeIfAbsent(entity.getClass(), PrimitiveRegistration::getMinimalPrimitiveForEntity);

    PrimitiveMethod method = primitiveMethodMap.get(opName);
    if (method == null) {
      throw new IllegalStateException("No primitive named " + opName + " for entity of class " + entity.getClass().getName() +
                                      ". This usually means the entity is not an instance of the exercise's real entity subclass.");
    }

    Object[] args = (Object[])ValueSerializer.deserialize(opArgsSegment);

    for (int i = 0; i < args.length; i++) {
      Object rawArg    = args[i];
      Class<?> argType = method.parameters().get(i).type();

      if (argType.isEnum()) {
        try {
          args[i] = ((Object[])argType.getMethod("values").invoke(null))[(int)rawArg];
        } catch (NoSuchMethodException e) {
          throw new RuntimeException(e);
        }
      } else if (argType == String.class &&
                 (rawArg instanceof Integer || rawArg instanceof Double || rawArg instanceof Boolean || rawArg instanceof Color || rawArg instanceof Point)) {
        // Python has no static typing, so nothing forces student code to write e.g. str(i) before passing one of
        // ValueSerializer.py's atomic types (int, float, bool, Color, Point) where a String is expected.
        // Rather than requiring that from every Python exercise, convert here with a plain toString(): the serialized
        // value is unambiguously typed (ValueSerializer tags it), so this convertion cannot hide a real type error.
        args[i] = String.valueOf(rawArg);
      }
    }

    Method javaMethod = method.method();
    Object returnValue;
    try {
      returnValue = javaMethod.invoke(entity, args);
    } catch (InvocationTargetException e) {
      String reserializedArgs = serialize(args);
      String argMsg           = opArgsSegment.equals(reserializedArgs) ? opArgsSegment : opArgsSegment + " (changed to " + serialize(args) + ")";
      throw new IllegalArgumentException("Calling " + method.name() + "(" + argMsg + ") raised an exception: " + e.getCause(), e);
    } catch (IllegalArgumentException e) {
      String reserializedArgs = serialize(args);
      String argMsg           = opArgsSegment.equals(reserializedArgs) ? opArgsSegment : opArgsSegment + " (changed to " + serialize(args) + ")";
      throw new IllegalArgumentException("Cannot apply parameters " + argMsg + " to " + method.name() + "()", e);
    }

    try {
      if (method.hasReturn()) {

        if (returnValue instanceof Enum<?>) {
          returnValue = ((Enum<?>)returnValue).ordinal();
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
}
