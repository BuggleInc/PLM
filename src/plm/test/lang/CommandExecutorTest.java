package plm.test.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;
import plm.core.ValueSerializer;
import plm.core.lang.primitives.Primitive;
import plm.universe.CommandExecutor;
import plm.universe.Entity;
import plm.universe.EntityPrimitivesBase;
import plm.universe.ExplainedException;
import plm.universe.PrimitiveRefusedException;

/** Tests of {@link CommandExecutor}: the commands are "<primitive name> <serialized arguments>". */
public class CommandExecutorTest {

  public enum Mode { LOW, HIGH }

  public interface RecordingPrimitives extends EntityPrimitivesBase {
    @Primitive void setFlag(boolean b);
    @Primitive int twice(int x);
    @Primitive String greet(String name);
    @Primitive Mode flip(Mode mode);
    @Primitive void refuse();
    @Primitive void deadlock();
    @Primitive void unsupported();
    @Primitive void crash();
  }

  public static class RecordingEntity extends Entity implements RecordingPrimitives {
    public boolean flag;
    public int calls;

    @Override public void setFlag(boolean b)
    {
      flag = b;
      calls++;
    }
    @Override public int twice(int x)
    {
      calls++;
      return 2 * x;
    }
    @Override public String greet(String name) { return "Hello " + name; }
    @Override public Mode flip(Mode mode) { return mode == Mode.LOW ? Mode.HIGH : Mode.LOW; }
    @Override public void refuse() { throw new PrimitiveRefusedException("Not in this exercise"); }
    @Override public void deadlock() { throw new ExplainedException("Deadlock: you wait forever"); }
    @Override public void unsupported() { throw new UnsupportedOperationException("Not implemented"); }
    @Override public void crash() { throw new IllegalStateException("oops"); }
    @Override public void run() {}
  }

  public static class GatedEntity extends RecordingEntity {
    public GatedEntity() { forbid("twice", "Think harder."); }
  }

  /** The command that the student's process sends to call the primitive with these arguments */
  public static String command(String primitive, Object... args) { return primitive + " " + ValueSerializer.serialize(args); }

  /** Executes the command and returns what the PLM wrote back to the student's process */
  static String execute(Entity entity, String command) throws Exception
  {
    StringWriter written = new StringWriter();
    BufferedWriter out   = new BufferedWriter(written);
    CommandExecutor.command(entity, command, out);
    out.flush();
    return written.toString();
  }

  @Test public void returnValueIsWrittenBack() throws Exception
  {
    assertEquals(ValueSerializer.serialize(8) + "\n", execute(new RecordingEntity(), command("twice", 4)));
  }

  @Test public void primitiveWithoutReturnValueWritesNothing() throws Exception
  {
    RecordingEntity entity = new RecordingEntity();

    assertEquals("", execute(entity, command("setFlag", true)));
    assertTrue(entity.flag);
  }

  @Test public void spacesInArgumentsAreKept() throws Exception
  {
    assertEquals(ValueSerializer.serialize("Hello Oh Boy!") + "\n", execute(new RecordingEntity(), command("greet", "Oh Boy!")));
  }

  @Test public void atomicValuesAreConvertedWhereAStringIsExpected() throws Exception
  {
    assertEquals(ValueSerializer.serialize("Hello 5") + "\n", execute(new RecordingEntity(), command("greet", 5)));
  }

  @Test public void enumsAreExchangedAsOrdinals() throws Exception
  {
    assertEquals(ValueSerializer.serialize(0) + "\n", execute(new RecordingEntity(), command("flip", Mode.HIGH.ordinal())));
  }

  @Test public void unknownPrimitiveIsRejected()
  {
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> execute(new RecordingEntity(), command("nope")));

    assertTrue(e.getMessage().contains("nope"), e.getMessage());
  }

  @Test public void forbiddenPrimitiveIsRefusedBeforeBeingCalled()
  {
    GatedEntity entity = new GatedEntity();

    PrimitiveRefusedException e = assertThrows(PrimitiveRefusedException.class, () -> execute(entity, command("twice", 4)));

    assertTrue(e.getMessage().contains("twice"), e.getMessage());
    assertTrue(e.getMessage().contains("Think harder."), e.getMessage());
    assertEquals(0, entity.calls);
  }

  @Test public void refusalByTheEntityIsReportedAsIs()
  {
    PrimitiveRefusedException e = assertThrows(PrimitiveRefusedException.class, () -> execute(new RecordingEntity(), command("refuse")));

    assertEquals("Not in this exercise", e.getMessage());
  }

  @Test public void explainedErrorIsReportedAsIs()
  {
    ExplainedException e = assertThrows(ExplainedException.class, () -> execute(new RecordingEntity(), command("deadlock")));

    assertEquals("Deadlock: you wait forever", e.getMessage());
  }

  @Test public void unsupportedOperationIsNotARefusal()
  {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> execute(new RecordingEntity(), command("unsupported")));

    assertTrue(e.getMessage().startsWith("Calling unsupported("), e.getMessage());
  }

  @Test public void otherExceptionsAreReportedWithTheCall()
  {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> execute(new RecordingEntity(), command("crash")));

    assertTrue(e.getMessage().startsWith("Calling crash("), e.getMessage());
    assertTrue(e.getMessage().contains("oops"), e.getMessage());
  }

  @Test public void argumentsOfTheWrongTypeAreReported()
  {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> execute(new RecordingEntity(), command("twice", "four")));

    assertTrue(e.getMessage().startsWith("Cannot apply parameters"), e.getMessage());
  }
}
