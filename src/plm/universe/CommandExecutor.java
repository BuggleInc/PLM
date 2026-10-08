package plm.universe;

import static plm.core.ValueSerializer.serialize;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import plm.core.ValueSerializer;
import plm.core.lang.primitives.PrimitiveMethod;
import plm.core.lang.primitives.PrimitiveRegistration;
import plm.core.model.Game;

public final class CommandExecutor {
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

    String forbiddenReason = entity.getForbiddenPrimitives().get(opName);
    if (forbiddenReason != null)
      throw new UnsupportedOperationException(Game.i18n.tr("Sorry Dave, I cannot let you use {0} in this exercise. {1}", opName, forbiddenReason));

    PrimitiveMethod method = PrimitiveRegistration.getMinimalPrimitiveForEntity(entity.getClass()).get(opName);
    if (method == null) {
      throw new IllegalStateException("No primitive named " + opName + " for entity of class " + entity.getClass().getName() +
                                      ". This usually means the entity is not an instance of the exercise's real entity subclass.");
    }

    Object[] args = deserializeArguments(method, opArgsSegment);

    Object returnValue;
    try {
      returnValue = method.method().invoke(entity, args);
    } catch (InvocationTargetException e) {
      // An entity refusing a call on purpose (e.g. an override forbidding some arguments) explains itself: its message is shown as is
      if (e.getCause() instanceof UnsupportedOperationException refusal)
        throw refusal;
      throw new IllegalArgumentException("Calling " + method.name() + "(" + describeArguments(opArgsSegment, args) + ") raised an exception: " + e.getCause(),
                                         e);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Cannot apply parameters " + describeArguments(opArgsSegment, args) + " to " + method.name() + "()", e);
    }

    if (!method.hasReturn())
      return;
    if (returnValue instanceof Enum<?>)
      returnValue = ((Enum<?>)returnValue).ordinal();
    try {
      out.write(serialize(returnValue) + "\n");
      out.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Deserializes the arguments, converting the ones that the serialization cannot type exactly: enum ordinals and atomic values given as Strings. */
  private static Object[] deserializeArguments(PrimitiveMethod method, String serializedArgs) throws InvocationTargetException, IllegalAccessException
  {
    Object[] args = (Object[])ValueSerializer.deserialize(serializedArgs);

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
    return args;
  }

  /** The arguments as sent by the student, followed by the converted ones if they differ. */
  private static String describeArguments(String sent, Object[] args)
  {
    String converted = serialize(args);
    return sent.equals(converted) ? sent : sent + " (changed to " + converted + ")";
  }
}
